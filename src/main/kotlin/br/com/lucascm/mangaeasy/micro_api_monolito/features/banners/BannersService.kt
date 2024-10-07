package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.dtos.CreateBannerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.entities.BannersEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.repositories.BannersRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.BucketRecommendationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class BannersService {
    @Autowired
    lateinit var bucketRecommendationsRepository: BucketRecommendationsRepository

    @Autowired
    lateinit var bannersRepository: BannersRepository
    fun updateImage(id: String, image: MultipartFile): BannersEntity {
        val find = bannersRepository.findById(id)
        if (!find.isPresent) throw BusinessException("Recomendação não encontrada")
        val entity = find.get()
        val imageResult: String?
        bucketRecommendationsRepository.saveImage(entity.id!!, image)
        imageResult = bucketRecommendationsRepository.getLinkImage(entity.id)
        return bannersRepository.save(
            entity.copy(
                image = imageResult,
                updatedAt = Date().time
            )
        )
    }

    fun create(body: CreateBannerDto): BannersEntity {
        return bannersRepository.save(
            BannersEntity(
                updatedAt = Date().time,
                link = body.link,
                image = body.image,
                createdAt = Date().time
            )
        )
    }

    fun update(body: CreateBannerDto, id: String): BannersEntity {
        val find = bannersRepository.findById(id)
        if (!find.isPresent) {
            throw BusinessException("Banner não encontrado")
        }
        val banner = find.get().copy(
            updatedAt = Date().time,
            link = body.link,
            image = body.image
        )

        return bannersRepository.save(banner)
    }

    fun findById(id: String): BannersEntity {
        return bannersRepository.findById(id).getOrNull()
            ?: throw BusinessException("Banner não encontrado")
    }

    fun findAll(page: Int): List<BannersEntity> {
        return bannersRepository.findAll(
            PageRequest.of(
                page,
                25,
                Sort.by(BannersEntity::updatedAt.name).descending()
            )
        ).content
    }

    fun delete(id: String, image: MultipartFile) {
        val find = bannersRepository.findById(id)
        if (!find.isPresent) throw BusinessException("Recomendação não encontrada")
        val entity = find.get()
        bucketRecommendationsRepository.deleteImage(entity.id!!)
        bannersRepository.deleteById(id)
    }
}