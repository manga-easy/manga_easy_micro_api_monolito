package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.LatestMangaEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LatestMangaRepository : MongoRepository<LatestMangaEntity, Long> {
    fun findByIdhostAndVersionApp(idHost: Int, versionApp: String?): LatestMangaEntity?
}