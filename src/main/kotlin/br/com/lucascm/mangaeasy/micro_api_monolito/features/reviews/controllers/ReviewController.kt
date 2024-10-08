package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ListReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ReviewRatingStatistics
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.services.ReviewService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/catalogs/{catalogId}/reviews")
@Tag(name = "Reviews")
class ReviewController {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var reviewService: ReviewService


    @GetMapping("/v1")
    fun list(@PathVariable catalogId: String, @RequestParam page: Int?): List<ListReviewDto> {
        return reviewService.list(catalogId, page ?: 0)
    }

    @GetMapping("/v1/users/{userId}")
    fun findByUser(
        @PathVariable catalogId: String,
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable userId: String,
    ): ReviewEntity? {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        return reviewService.findByCatalogIdAndUserId(catalogId, userId)
    }

    @GetMapping("/v1/rating-statistics")
    fun getRatingStatistics(@PathVariable catalogId: String): ReviewRatingStatistics {
        return reviewService.ratingStatisticsByCatalog(catalogId)
    }

    @GetMapping("/v1/last")
    fun last(@PathVariable catalogId: String): List<ListReviewDto> {
        return reviewService.listLast(catalogId)
    }

    @PostMapping("/v1")
    fun create(
        @PathVariable catalogId: String,
        @RequestBody body: ReviewDto,
        @AuthenticationPrincipal userAuth: UserAuth,
    ): ReviewEntity {
        return reviewService.create(body, catalogId, userAuth.userId)
    }

    @PutMapping("/v1/{id}")
    fun update(
        @RequestBody body: ReviewDto,
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth,
    ): ReviewEntity {
        return reviewService.update(body, id)
    }
}