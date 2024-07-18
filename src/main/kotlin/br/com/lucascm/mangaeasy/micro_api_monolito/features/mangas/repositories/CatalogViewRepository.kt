package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogViewEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CatalogViewRepository : JpaRepository<CatalogViewEntity, Long> {
    fun findByCatalogIdAndUserId(catalogId: String, userId: String): CatalogViewEntity?

    @Query(
        """
            SELECT COUNT(*)
            FROM CatalogViewEntity dc 
            WHERE dc.catalogId = :catalogId
        """
    )
    fun countByCatalogId(@Param("catalogId") catalogId: String): Long

    @Query(
        """
        SELECT catalogId
            FROM CatalogViewEntity
            WHERE YEARWEEK(FROM_UNIXTIME(createdAt / 1000), 1) = YEARWEEK(CURDATE(), 1)
            GROUP BY catalogId
            ORDER BY COUNT(*) DESC
        LIMIT 1
        """
    )
    fun mostMangaReadWeekly(): String?
}