package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.LikeReviewEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LikeReviewRepository : JpaRepository<LikeReviewEntity, Long> {
    fun findByIdAndUserId(id: Long, userId: String): LikeReviewEntity?

    @Query(
        """
            SELECT COUNT(*)
            FROM LikeReviewEntity dc 
            WHERE dc.reviewId = :reviewId
        """
    )
    fun countByReviewId(@Param("reviewId") reviewId: Long): Long
}