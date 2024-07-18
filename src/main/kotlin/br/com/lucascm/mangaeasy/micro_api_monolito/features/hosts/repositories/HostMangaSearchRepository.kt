package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostMangaSearchEntity
import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.stereotype.Repository

@Repository
interface HostMangaSearchRepository : KeyValueRepository<HostMangaSearchEntity, String> {

}