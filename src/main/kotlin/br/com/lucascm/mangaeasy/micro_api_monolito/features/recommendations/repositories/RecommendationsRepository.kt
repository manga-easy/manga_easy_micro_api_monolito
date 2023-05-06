package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import org.springframework.data.jpa.repository.JpaRepository



interface RecommendationsRepository : JpaRepository<RecommendationsEntity, Long> {

}