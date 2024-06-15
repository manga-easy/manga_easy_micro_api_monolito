package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.FoundReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.LikeReviewEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.LikeReviewRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.ReviewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController("like review")
@RequestMapping("/review/{id}")
class LikeReviewController {
    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var likeReviewRepository: LikeReviewRepository

    @PostMapping("/v1/like")
    fun create(@PathVariable id: Long, @AuthenticationPrincipal userAuth: UserAuth): LikeReviewEntity {
        val review = reviewRepository.findById(id)
        if (!review.isPresent) {
            throw BusinessException("Avaliação não encontrado: $id")
        }
        return likeReviewRepository.findByIdAndUserId(id, userAuth.userId)
            ?: likeReviewRepository.save(
                LikeReviewEntity(
                    userId = userAuth.userId,
                    reviewId = id,
                    createdAt = Date().time,
                )
            )
    }

    @GetMapping("/v1/like")
    @ResponseBody
    fun get(@PathVariable id: Long, @AuthenticationPrincipal userAuth: UserAuth): FoundReviewDto {
        val review = reviewRepository.findById(id)
        if (!review.isPresent) {
            throw BusinessException("Avaliação não encontrado: $id")
        }
        val result = likeReviewRepository.findByIdAndUserId(id, userAuth.userId)
        return FoundReviewDto(liked = result != null)
    }

    @DeleteMapping("/v1/like")
    @ResponseBody
    fun delete(@PathVariable id: Long, @AuthenticationPrincipal userAuth: UserAuth) {
        val result = likeReviewRepository.findByIdAndUserId(
            id, userAuth.userId
        ) ?: throw BusinessException("Like não encontrado")
        return likeReviewRepository.deleteById(result.id!!)
    }
}