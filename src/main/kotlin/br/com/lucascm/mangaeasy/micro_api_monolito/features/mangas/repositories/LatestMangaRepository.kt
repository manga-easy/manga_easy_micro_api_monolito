package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.core.configs.RedisRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.LatestMangaEntity
import org.springframework.stereotype.Repository

@Repository
interface LatestMangaRepository : RedisRepository<LatestMangaEntity, String> {

}