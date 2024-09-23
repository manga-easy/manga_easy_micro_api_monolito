package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.LatestMangaEntity
import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.stereotype.Repository

@Repository
interface LatestMangaRepository : KeyValueRepository<LatestMangaEntity, String> {

}