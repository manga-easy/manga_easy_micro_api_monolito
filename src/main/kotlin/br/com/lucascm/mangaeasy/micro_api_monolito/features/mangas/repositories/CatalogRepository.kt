package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CatalogRepository : JpaRepository<CatalogEntity, Long> {
    @Query("SELECT i FROM CatalogEntity i ")
    fun list(
        uniqueid: String?,
        author: String?,
        isAdult: Boolean,
        genres: List<String>,
        search: String?,
        yearFrom: Int?,
        yearAt: Int?,
        pageable: Pageable
    ): Page<CatalogEntity>

    fun findByUniqueid(uniqueid: String): CatalogEntity?
}