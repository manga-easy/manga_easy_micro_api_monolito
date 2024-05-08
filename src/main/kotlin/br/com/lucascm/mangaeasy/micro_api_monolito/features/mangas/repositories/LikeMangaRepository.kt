package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.LikeMangaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LikeMangaRepository : JpaRepository<LikeMangaEntity, Long> {
    fun findByUniqueidAndUserId(uniqueid: String, userId: String): LikeMangaEntity?
}