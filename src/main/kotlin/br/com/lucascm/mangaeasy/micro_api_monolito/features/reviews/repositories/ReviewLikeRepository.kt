package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewLikeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ReviewLikeRepository : JpaRepository<ReviewLikeEntity, Long> {
    fun findByReviewIdAndUserId(reviewId: String, userId: String): Optional<ReviewLikeEntity>

    @Query(
        """
            SELECT COUNT(*)
            FROM ReviewLikeEntity dc 
            WHERE dc.reviewId = :reviewId
        """
    )
    fun countByReviewId(@Param("reviewId") reviewId: String): Long
}