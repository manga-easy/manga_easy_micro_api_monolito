package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.core.configs.RedisRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.RankingEntity
import org.springframework.stereotype.Repository

@Repository
interface RankingCache : RedisRepository<RankingEntity, String> {
}