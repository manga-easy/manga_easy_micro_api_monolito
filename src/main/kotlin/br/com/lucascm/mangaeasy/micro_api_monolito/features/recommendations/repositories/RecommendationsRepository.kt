package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface RecommendationsRepository : JpaRepository<RecommendationsEntity, String> {
    fun findByUniqueid(uniqueid: String): RecommendationsEntity?
    fun findByTitle(title: String): RecommendationsEntity?
}