package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.ViewMangaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ViewMangaRepository : JpaRepository<ViewMangaEntity, Long> {
    fun findByUniqueidAndUserId(uniqueid: String, userId: String): ViewMangaEntity?
}