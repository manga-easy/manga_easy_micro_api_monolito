package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ReviewRepository : JpaRepository<ReviewEntity, String> {
    fun findByCatalogIdAndUserId(
        catalogId: String,
        userId: String,
    ): ReviewEntity?

    fun findByCatalogId(
        catalogId: String,
        pageable: Pageable,
    ): List<ReviewEntity>

    @Query(
        """
            SELECT COUNT(*)
            FROM ReviewEntity dc 
            WHERE dc.catalogId = :catalogId
        """
    )
    fun countByCatalogId(@Param("catalogId") catalogId: String): Long
}