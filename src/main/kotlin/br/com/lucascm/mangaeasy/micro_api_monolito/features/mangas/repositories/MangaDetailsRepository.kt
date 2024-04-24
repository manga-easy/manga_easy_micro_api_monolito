package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.core.configs.RedisRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.MangaDetailsEntity
import org.springframework.stereotype.Repository

@Repository
interface MangaDetailsRepository : RedisRepository<MangaDetailsEntity, String> {}