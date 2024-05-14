package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.ViewMangaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ViewMangaRepository : JpaRepository<ViewMangaEntity, Long> {
    fun findByUniqueidAndUserId(uniqueid: String, userId: String): ViewMangaEntity?

    @Query(
        """
            SELECT COUNT(*)
            FROM ViewMangaEntity dc 
            WHERE dc.uniqueid = :uniqueid
        """
    )
    fun countByUniqueid(@Param("uniqueid") uniqueid: String): Long

    @Query(
        """
        SELECT uniqueid
            FROM ViewMangaEntity
            WHERE YEARWEEK(FROM_UNIXTIME(createdAt / 1000), 1) = YEARWEEK(CURDATE(), 1)
            GROUP BY uniqueid
            ORDER BY COUNT(*) DESC
        LIMIT 1
        """
    )
    fun mostMangaReadWeekly(): String?
}