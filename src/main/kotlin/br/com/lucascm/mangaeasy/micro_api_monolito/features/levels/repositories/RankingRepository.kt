package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.RankingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RankingRepository : JpaRepository<RankingEntity, String> {
    fun findByUserId(userId: String): RankingEntity?
}