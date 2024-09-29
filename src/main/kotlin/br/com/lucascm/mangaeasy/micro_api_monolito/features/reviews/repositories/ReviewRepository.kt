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

    fun findByCatalogIdAndCommentaryIsNotNull(
        catalogId: String,
        pageable: Pageable? = null,
    ): List<ReviewEntity>

    @Query(
        """
            SELECT COUNT(*)
            FROM ReviewEntity dc 
            WHERE dc.catalogId = :catalogId
        """
    )
    fun countReviewsByCatalog(@Param("catalogId") catalogId: String): Long

    @Query(
        """
            SELECT COUNT(*)
            FROM ReviewEntity dc 
            WHERE dc.catalogId = :catalogId
                 AND commentary is not null
        """
    )
    fun countCommentsByCatalog(@Param("catalogId") catalogId: String): Long

    @Query(
        """
            SELECT ROUND(AVG(dc.rating), 1)
            FROM ReviewEntity dc 
            WHERE dc.catalogId = :catalogId
        """
    )
    fun ratingByCatalog(@Param("catalogId") catalogId: String): Double

    @Query(
        """
            SELECT
                ROUND(AVG(r.rating), 1) AS rating,
                COUNT(*) AS quantity,
                SUM(CASE WHEN r.rating = 1 THEN 1 ELSE 0 END) AS quantity1,
                SUM(CASE WHEN r.rating = 2 THEN 1 ELSE 0 END) AS quantity2,
                SUM(CASE WHEN r.rating = 3 THEN 1 ELSE 0 END) AS quantity3,
                SUM(CASE WHEN r.rating = 4 THEN 1 ELSE 0 END) AS quantity4,
                SUM(CASE WHEN r.rating = 5 THEN 1 ELSE 0 END) AS quantity5,
                ROUND(AVG(CASE WHEN r.rating = 1 THEN r.rating ELSE 0 END), 1) AS rating1,
                ROUND(AVG(CASE WHEN r.rating = 2 THEN r.rating ELSE 0 END), 1) AS rating2,
                ROUND(AVG(CASE WHEN r.rating = 3 THEN r.rating ELSE 0 END), 1) AS rating3,
                ROUND(AVG(CASE WHEN r.rating = 4 THEN r.rating ELSE 0 END), 1) AS rating4,
                ROUND(AVG(CASE WHEN r.rating = 5 THEN r.rating ELSE 0 END), 1) AS rating5
            FROM review r
            WHERE catalog_id = :catalogId
        """,
        nativeQuery = true
    )
    fun ratingStatisticsByCatalog(@Param("catalogId") catalogId: String): Map<String, Any>
    fun findTop10ByCatalogIdAndCommentaryIsNotNullOrderByCreatedAtDesc(catalogId: String): List<ReviewEntity>

}