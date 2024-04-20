package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface CatalogRepository : JpaSpecificationExecutor<CatalogEntity>, JpaRepository<CatalogEntity, Long> {

    fun findByUniqueid(uniqueid: String): CatalogEntity?

    @Query(
        """
            DELETE FROM `catalog` WHERE created_at = updated_at and created_at > UNIX_TIMESTAMP(NOW() - INTERVAL 60 DAY)
        """,
        nativeQuery = true
    )
    fun deleteMangaInactive()

    @Query(
        """
            SELECT * FROM `catalog` c 
            WHERE (genres NOT LIKE '%adult%' or ?1)
            ORDER BY RAND()
            LIMIT 1;
        """,
        nativeQuery = true
    )
    fun findMangaRandom(isAdult: Boolean): CatalogEntity
}