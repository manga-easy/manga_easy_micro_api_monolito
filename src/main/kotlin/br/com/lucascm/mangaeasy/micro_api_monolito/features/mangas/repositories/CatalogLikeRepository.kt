package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogLikeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CatalogLikeRepository : JpaRepository<CatalogLikeEntity, Long> {
    fun findByCatalogIdAndUserId(catalogId: String, userId: String): CatalogLikeEntity?

    @Query(
        """
            SELECT COUNT(*)
            FROM CatalogLikeEntity dc 
            WHERE dc.catalogId = :catalogId
        """
    )
    fun countByCatalogId(@Param("catalogId") catalogId: String): Long
}