package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.LatestMangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.MandaDetailsEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MangaDetailsRepository : MongoRepository<MandaDetailsEntity, Long> {
    fun findByIdhostAndUniqueid(idhost: Int, uniqueid: String): MandaDetailsEntity?
}