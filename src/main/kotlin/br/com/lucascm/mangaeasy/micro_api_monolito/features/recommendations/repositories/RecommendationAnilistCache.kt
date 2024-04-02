package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories

import CacheAnilistEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.configs.RedisRepository
import org.springframework.stereotype.Repository

@Repository
interface RecommendationAnilistCache : RedisRepository<CacheAnilistEntity, String> {}