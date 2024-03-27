package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories

import CacheAnilistEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RecommendationAnilistCache : CrudRepository<CacheAnilistEntity, Long> {
    fun findByTitle(title: String): CacheAnilistEntity?
}