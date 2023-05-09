package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AchievementsRepository : JpaRepository<AchievementsEntity, Long> {
    fun findAllByUid(
        uid: String
    ): List<AchievementsEntity>

    fun findAllByDisponivel(
        available: Boolean
    ): List<AchievementsEntity>
}