package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.services

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.RedisCacheName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogLikeRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogViewRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.ReviewRepository
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull


@Service
class CatalogService {
    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var viewMangaRepository: CatalogViewRepository

    @Autowired
    lateinit var likeMangaRepository: CatalogLikeRepository

    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Cacheable(RedisCacheName.LIST_CATALOG)
    fun list(
        genres: List<String>,
        search: String? = null,
        author: String? = null,
        page: Int? = null,
        limit: Int? = null,
        yearAt: Int? = null,
        yearFrom: Int? = null,
        scans: String? = null,
        isAdult: Boolean,
    ): List<CatalogEntity> {
        val result = catalogRepository.findAll(
            Specification(fun(
                root: Root<CatalogEntity>,
                _: CriteriaQuery<*>,
                builder: CriteriaBuilder,
            ): Predicate? {
                val predicates = mutableListOf<Predicate>()
                if (genres.isNotEmpty()) {
                    genres.forEach {
                        predicates.add(
                            builder.like(
                                builder.lower(root.get("genres")),
                                "%" + it.lowercase() + "%"
                            )
                        )
                    }
                }
                if (search != null) {
                    predicates.add(
                        builder.like(
                            builder.lower(root.get("name")),
                            "%" + search.lowercase() + "%"
                        )
                    )
                }
                if (author != null) {
                    predicates.add(
                        builder.like(
                            builder.lower(root.get("author")),
                            "%" + author.lowercase() + "%"
                        )
                    )
                }
                if (yearAt != null && yearFrom != null) {
                    predicates.add(builder.between(root.get("year"), yearAt, yearFrom))
                }
                if (scans != null) {
                    predicates.add(
                        builder.like(
                            builder.lower(root.get("scans")),
                            "%" + scans.lowercase() + "%"
                        )
                    )
                }
                if (!isAdult) {
                    predicates.add(
                        builder.notLike(
                            builder.lower(root.get("genres")),
                            "%adult%"
                        )
                    )
                }
                return builder.and(*predicates.toTypedArray())
            }),
            PageRequest.of(page ?: 0, limit ?: 25)
        )
        return result.content
    }

    @Cacheable(RedisCacheName.CATALOG, key = "#uniqueId")
    fun getByuniqueId(uniqueId: String): CatalogEntity? {
        val result = catalogRepository.findByUniqueid(uniqueId) ?: return null
        return updateTotals(result)
    }

    @Cacheable(RedisCacheName.GET_MANGA_WEEKLY)
    fun mostMangaWeekly(): CatalogEntity {
        val catalogId = viewMangaRepository.mostMangaReadWeekly()
            ?: return catalogRepository.findMangaRandom(false)
        var result = catalogRepository.findById(catalogId).getOrNull()
        if (result == null) {
            result = catalogRepository.findMangaRandom(false)
        }
        return result
    }

    @Cacheable(RedisCacheName.SUGGESTIVE_MANGA_NAME)
    fun suggestiveName(name: String): List<String> {
        return catalogRepository.findNames(name)
    }

    private fun updateTotals(catalog: CatalogEntity): CatalogEntity {
        val totalLikes = likeMangaRepository.countByCatalogId(catalog.id!!)
        val totalViews = viewMangaRepository.countByCatalogId(catalog.id)
        val totalReviews = reviewRepository.countReviewsByCatalog(catalog.id)

        val totalComments = if (totalReviews.toInt() == 0) 0 else
            reviewRepository.countCommentsByCatalog(catalog.id)

        val rating = if (totalReviews.toInt() == 0) 0.0 else
            reviewRepository.ratingByCatalog(catalog.id)

        return catalogRepository.save(
            catalog.copy(
                ratio = rating,
                totalReviews = totalReviews,
                totalComments = totalComments,
                totalLikes = totalLikes,
                totalViews = totalViews,
            )
        )
    }
}