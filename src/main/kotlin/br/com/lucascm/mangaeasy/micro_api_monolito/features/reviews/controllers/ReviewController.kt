package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ListReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewMangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.ReviewRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.services.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/review")
class ReviewController {


    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var reviewService: ReviewService


    @GetMapping("/v1/{uniqueid}")
    fun list(@PathVariable uniqueid: String, @RequestParam page: Int?): List<ListReviewDto> {
        return reviewService.list(uniqueid, page ?: 0)
    }

    @PostMapping("/v1/{uniqueid}")
    @ResponseBody
    fun create(
        @RequestBody body: ReviewDto,
        @PathVariable uniqueid: String,
        authentication: Authentication,
    ): ReviewMangaEntity {
        val userId = authentication.principal.toString()
        catalogRepository.findByUniqueid(uniqueid)
            ?: throw BusinessException("Manga não encontrado: $uniqueid")
        val review = reviewRepository.findByUniqueidAndUserId(uniqueid, userId)
        if (review != null) {
            throw BusinessException("Ja foi enviado: $uniqueid")
        }
        return reviewRepository.save(
            ReviewMangaEntity(
                uniqueid = uniqueid,
                createdAt = Date().time,
                totalLikes = 0,
                userId = userId,
                updatedAt = Date().time,
                commentary = body.commentary,
                value = body.value,
                hasSpoiler = body.hasSpoiler,
                hasUpdated = false,
            )
        )
    }

    @PutMapping("/v1/{uniqueid}")
    @ResponseBody
    fun update(
        @RequestBody body: ReviewDto,
        @PathVariable uniqueid: String,
        authentication: Authentication,
    ): ReviewMangaEntity {
        val userId = authentication.principal.toString()
        val review = reviewRepository.findByUniqueidAndUserId(uniqueid, userId)
            ?: throw BusinessException("Avaliação não encontrada: $uniqueid")
        return reviewRepository.save(
            review.copy(
                commentary = body.commentary,
                value = body.value,
                updatedAt = Date().time,
                hasSpoiler = body.hasSpoiler,
                hasUpdated = true,
            )
        )
    }
}