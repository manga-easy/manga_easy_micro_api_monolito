package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.LikeReviewEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.LikeReviewRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.ReviewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/review/{id}")
class LikeReviewController {
    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var likeReviewRepository: LikeReviewRepository

    @PostMapping("/v1/like")
    @ResponseBody
    fun create(@PathVariable id: Long, authentication: Authentication): ResultEntity {
        return try {
            val userId = authentication.principal.toString()
            val review = reviewRepository.findById(id)
            if (!review.isPresent) {
                throw BusinessException("Avaliação não encontrado: $id")
            }
            val result = likeReviewRepository.findByIdAndUserId(id, userId)
            if (result == null) {
                val resultSave = likeReviewRepository.save(
                    LikeReviewEntity(
                        userId = userId,
                        reviewId = id,
                        createdAt = Date().time,
                    )
                )
                return ResultEntity(listOf(resultSave))
            }
            ResultEntity(listOf(result))
        } catch (e: Exception) {
            HandleExceptions().handleCatch(e)
        }
    }

    @GetMapping("/v1/like")
    @ResponseBody
    fun get(@PathVariable id: Long, authentication: Authentication): ResultEntity {
        return try {
            val userId = authentication.principal.toString()
            val review = reviewRepository.findById(id)
            if (!review.isPresent) {
                throw BusinessException("Avaliação não encontrado: $id")
            }
            val result = likeReviewRepository.findByIdAndUserId(id, userId)
            ResultEntity(listOf(result != null))
        } catch (e: Exception) {
            HandleExceptions().handleCatch(e)
        }
    }

    @DeleteMapping("/v1/like")
    @ResponseBody
    fun delete(@PathVariable id: Long, authentication: Authentication): ResultEntity {
        return try {
            val userId = authentication.principal.toString()
            val result = likeReviewRepository.findByIdAndUserId(
                id, userId
            )
                ?: throw BusinessException("Like não encontrado")
            val resultSave = likeReviewRepository.deleteById(result.id!!)
            ResultEntity(listOf(resultSave))
        } catch (e: Exception) {
            HandleExceptions().handleCatch(e)
        }
    }
}