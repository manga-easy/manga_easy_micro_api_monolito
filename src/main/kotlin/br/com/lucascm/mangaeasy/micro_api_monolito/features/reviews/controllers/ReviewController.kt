package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ListReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.ReviewRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.services.ReviewService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/catalogs/{catalogId}/reviews")
@Tag(name = "Reviews")
class ReviewController {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var reviewService: ReviewService


    @GetMapping("/v1")
    fun list(@PathVariable catalogId: String, @RequestParam page: Int?): List<ListReviewDto> {
        return reviewService.list(catalogId, page ?: 0)
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
        val review = reviewRepository.findByCatalogIdAndUserId(catalogId, userAuth.userId)
        if (review != null) {
            throw BusinessException("Review já foi feito: ${review.catalogId}")
        }
        return reviewRepository.save(
            ReviewEntity(
                catalogId = catalogId,
                createdAt = Date().time,
                totalLikes = 0,
                userId = userAuth.userId,
                updatedAt = Date().time,
                commentary = body.commentary,
                rating = body.rating,
                hasSpoiler = body.hasSpoiler,
                hasUpdated = false
            )
        )
    }

    @PutMapping("/v1/{id}")
    fun update(
        @RequestBody body: ReviewDto,
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth,
    ): ReviewEntity {
        val review = reviewRepository.findById(id).getOrNull()
            ?: throw BusinessException("Avaliação não encontrada: $id")
        return reviewRepository.save(
            review.copy(
                commentary = body.commentary,
                rating = body.rating,
                updatedAt = Date().time,
                hasSpoiler = body.hasSpoiler,
                hasUpdated = true
            )
        )
    }
}