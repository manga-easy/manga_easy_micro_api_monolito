package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewMangaEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ReviewRepository : JpaRepository<ReviewMangaEntity, Long> {
    fun findByUniqueidAndUserId(
        uniqueid: String,
        userId: String,
    ): ReviewMangaEntity?

    fun findByUniqueid(
        uniqueid: String,
        pageable: Pageable,
    ): List<ReviewMangaEntity>

    @Query(
        """
            SELECT COUNT(*)
            FROM ReviewMangaEntity dc 
            WHERE dc.uniqueid = :uniqueid
        """
    )
    fun countByUniqueid(@Param("uniqueid") uniqueid: String): Long
}