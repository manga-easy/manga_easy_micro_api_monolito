package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.services

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.RedisCacheName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.services.ProfileService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.CreateRecommendationDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.BucketRecommendationsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.RecommendationAnilistRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.RecommendationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class RecommendationsService {
    @Autowired
    lateinit var recommendationsRepository: RecommendationsRepository

    @Autowired
    lateinit var recommendationAnilistRepository: RecommendationAnilistRepository

    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var profileService: ProfileService

    @Autowired
    lateinit var bucketRecommendationsRepository: BucketRecommendationsRepository

    @Cacheable(RedisCacheName.RECOMMENDATIONS)
    fun list(page: Int): List<RecommendationsEntity> {
        val pageRequest = PageRequest.of(page, 25, Sort.by(RecommendationsEntity::updatedAt.name))
        val result = recommendationsRepository.findAll(pageRequest).content
        for (recommendation in result) {
            if (recommendation.artistId != null) {
                val user = profileService.findByUserId(recommendation.artistId)
                recommendation.artistName = user.name
            }
        }
        return result
    }

    fun findById(id: String): RecommendationsEntity {
        val recommendation = recommendationsRepository.findById(id).getOrNull()
            ?: throw BusinessException("Permission não encontrado")
        if (recommendation.artistId != null) {
            val user = profileService.findByUserId(recommendation.artistId)
            recommendation.artistName = user.name
        }
        return recommendation
    }

    @CacheEvict(value = [RedisCacheName.RECOMMENDATIONS], allEntries = true)
    fun update(body: CreateRecommendationDto, id: String): RecommendationsEntity {
        val result = recommendationsRepository.findById(id)
        if (!result.isPresent) throw BusinessException("Recomendação não encontrado")
        val user = profileService.findByUserId(body.artistId)
        return recommendationsRepository.save(
            RecommendationsEntity(
                title = body.title,
                updatedAt = Date().time,
                uniqueid = body.uniqueId,
                artistId = body.uniqueId,
                artistName = user.name,
            )
        )
    }

    @CacheEvict(value = [RedisCacheName.RECOMMENDATIONS], allEntries = true)
    fun updateImage(id: String, image: MultipartFile): RecommendationsEntity {
        val find = recommendationsRepository.findById(id)
        if (!find.isPresent) throw BusinessException("Recomendação não encontrada")
        val entity = find.get()
        bucketRecommendationsRepository.saveImage(entity.uniqueid, image)
        val imageResult: String?
        imageResult = bucketRecommendationsRepository.getLinkImage(entity.uniqueid)
        return recommendationsRepository.save(
            entity.copy(
                link = imageResult,
                updatedAt = Date().time
            )
        )
    }

    @CacheEvict(value = [RedisCacheName.RECOMMENDATIONS], allEntries = true)
    fun create(body: CreateRecommendationDto): RecommendationsEntity {
        val resultVCheck = recommendationsRepository.findByUniqueid(body.uniqueId)
        if (resultVCheck != null) {
            throw BusinessException("Mangá já tem recomendação")
        }
        val user = profileService.findByUserId(body.artistId)
        return recommendationsRepository.save(
            RecommendationsEntity(
                createdAt = Date().time,
                title = body.title,
                updatedAt = Date().time,
                uniqueid = body.uniqueId,
                artistId = user.name
            )
        )
    }

    @CacheEvict(value = [RedisCacheName.RECOMMENDATIONS], allEntries = true)
    fun delete(id: String) {
        val result = recommendationsRepository.findById(id).get()
        recommendationsRepository.deleteById(id)
        bucketRecommendationsRepository.deleteImage(result.uniqueid)
    }

    @Cacheable(RedisCacheName.RECOMMENDATIONS_ANILIST)
    fun anilist(title: String): List<RecommendationsEntity> {
        val recommendationAnilist = recommendationAnilistRepository.getRecommendationByTitle(title)
        val recommendations = mutableListOf<RecommendationsEntity>()
        for (e in recommendationAnilist) {
            val catalog = findCatalog(
                e.title.romaji ?: "",
                e.title.english ?: ""
            )
                ?: continue

            val recommendation = recommendationsRepository.findByTitle(catalog.name)
            if (recommendation != null) {
                recommendations.add(recommendation)
                continue
            }

            if (e.bannerImage != null) {
                recommendations.add(
                    RecommendationsEntity(
                        title = catalog.name,
                        link = e.bannerImage,
                        uniqueid = catalog.uniqueid,
                    )
                )
            }
        }
        return recommendations
    }

    private fun convertUniqueid(titleManga: String): String {
        // Lista de termos a serem removidos do título do manga
        val termos = listOf(
            "(br)",
            "(color)",
            "pt-br"
        )
        // Converter o título do manga para letras minúsculas
        var titleMangaLower = titleManga.toLowerCase()

        // Remover termos específicos do título do manga
        for (termo in termos) {
            titleMangaLower = titleMangaLower.replace(termo, "")
        }

        // Remover caracteres especiais do título do manga
        return titleMangaLower.replace(Regex("[^A-Za-z0-9]"), "")
    }

    private fun findCatalog(romanji: String, english: String): CatalogEntity? {
        if (romanji.isEmpty() && english.isEmpty()) return null
        val catalog = catalogRepository.findByUniqueid(convertUniqueid(romanji))
        if (catalog != null) return catalog
        return catalogRepository.findByUniqueid(convertUniqueid(english))
    }
}