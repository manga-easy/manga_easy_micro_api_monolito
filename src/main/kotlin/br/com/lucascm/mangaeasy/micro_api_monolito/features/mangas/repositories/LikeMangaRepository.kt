package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.LikeMangaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LikeMangaRepository : JpaRepository<LikeMangaEntity, Long> {
    fun findByUniqueidAndUserId(uniqueid: String, userId: String): LikeMangaEntity?

    @Query(
        """
            SELECT COUNT(*)
            FROM LikeMangaEntity dc 
            WHERE dc.uniqueid = :uniqueid
        """
    )
    fun countByUniqueid(@Param("uniqueid") uniqueid: String): Long
}