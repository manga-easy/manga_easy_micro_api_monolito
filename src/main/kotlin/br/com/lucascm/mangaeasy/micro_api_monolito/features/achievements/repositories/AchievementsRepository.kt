package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AchievementsRepository : JpaRepository<AchievementsEntity, Long> {
    fun findByUid(
        uid: String
    ): AchievementsEntity?

    fun findByDisponivelOrderByCreatedatDesc(
        available: Boolean
    ): List<AchievementsEntity>
}