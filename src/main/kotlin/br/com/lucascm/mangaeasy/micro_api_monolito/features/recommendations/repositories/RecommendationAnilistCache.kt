package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories

import MediaRecommendation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RecommendationAnilistCache : CrudRepository<MediaRecommendation, Long> {
}