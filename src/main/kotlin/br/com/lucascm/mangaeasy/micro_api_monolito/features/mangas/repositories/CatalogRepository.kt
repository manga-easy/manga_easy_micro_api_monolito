package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface CatalogRepository : JpaSpecificationExecutor<CatalogEntity>, JpaRepository<CatalogEntity, String> {

    fun findByUniqueid(uniqueid: String): CatalogEntity?

    @Query(
        """
            DELETE FROM `catalog` WHERE updated_at < (UNIX_TIMESTAMP(NOW() - INTERVAL 120 DAY) * 1000)
        """,
        nativeQuery = true
    )
    fun deleteMangaInactive()

    @Query(
        """
            SELECT name FROM `catalog` WHERE name LIKE CONCAT('%', :name, '%') LIMIT 6
        """,
        nativeQuery = true
    )
    fun findNames(@Param("name") name: String): List<String>

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