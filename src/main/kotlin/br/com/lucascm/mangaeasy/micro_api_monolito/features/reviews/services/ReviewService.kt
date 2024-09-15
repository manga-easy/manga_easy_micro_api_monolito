package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.services

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.RedisCacheName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ListReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.ReviewLikeRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.ReviewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ReviewService {
    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var profileRepository: ProfileRepository

    @Autowired
    lateinit var likeReviewRepository: ReviewLikeRepository

    @Cacheable(RedisCacheName.LIST_REVIEW)
    fun list(catalogId: String, page: Int): List<ListReviewDto> {
        val result = reviewRepository.findByCatalogId(
            catalogId,
            PageRequest.of(page, 25)
        )
        return getInfo(result)
    }

    @Cacheable(RedisCacheName.LIST_REVIEW_LAST)
    fun listLast(catalogId: String): List<ListReviewDto> {
        val result = reviewRepository.findTop10ByCatalogIdOrderByCreatedAtDesc(catalogId)
        return getInfo(result)
    }

    private fun getInfo(result: List<ReviewEntity>): List<ListReviewDto> {
        val list = mutableListOf<ListReviewDto>()
        for (review in result) {
            val profile = profileRepository.findByUserId(review.userId)
            val totalLike = likeReviewRepository.countByReviewId(review.catalogId)
            list.add(
                ListReviewDto(
                    review.copy(totalLikes = totalLike),
                    profile?.name,
                    profile?.picture
                )
            )
        }
        return list
    }
}