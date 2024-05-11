package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.services

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.RedisCacheName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ListReviewDto
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
    lateinit var likeReviewRepository: ReviewRepository

    @Cacheable(RedisCacheName.LIST_REVIEW)
    fun list(uniqueid: String, page: Int): List<ListReviewDto> {
        val result = reviewRepository.findByUniqueid(uniqueid, PageRequest.of(page, 25))
        val list = mutableListOf<ListReviewDto>()
        for (review in result) {
            val profile = profileRepository.findByUserID(review.userId)
            val totalLike = likeReviewRepository.countByUniqueid(review.uniqueid)
            list.add(
                ListReviewDto.fromEntity(
                    review.copy(totalLikes = totalLike),
                    profile?.name,
                    profile?.picture
                )
            )
        }
        return list
    }
}