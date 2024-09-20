package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.FoundReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewLikeEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.ReviewLikeRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/reviews/{reviewId}/likes")
@Tag(name = "Reviews")
class ReviewsLikesController {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var reviewLikeRepository: ReviewLikeRepository

    @PostMapping("/v1")
    fun create(@PathVariable reviewId: String, @AuthenticationPrincipal userAuth: UserAuth): ReviewLikeEntity {
        return reviewLikeRepository.findByReviewIdAndUserId(reviewId, userAuth.userId).getOrNull()
            ?: reviewLikeRepository.save(
                ReviewLikeEntity(
                    userId = userAuth.userId,
                    reviewId = reviewId,
                    createdAt = Date().time,
                )
            )
    }

    @GetMapping("/v1/users/{userId}")
    fun getByUser(@PathVariable userId: String, @PathVariable reviewId: String): FoundReviewDto {
        val result = reviewLikeRepository.findByReviewIdAndUserId(
            userId = userId,
            reviewId = reviewId
        )
        return FoundReviewDto(liked = result.isPresent)
    }

    @DeleteMapping("/v1/users/{userId}")
    fun delete(
        @PathVariable userId: String,
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable reviewId: String
    ) {
        val like = reviewLikeRepository.findByReviewIdAndUserId(
            userId = userId,
            reviewId = reviewId
        ).getOrNull()
            ?: throw BusinessException("Like não encontrado")
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        return reviewLikeRepository.deleteById(like.id!!)
    }
}