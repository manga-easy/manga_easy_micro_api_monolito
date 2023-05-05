package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AchievementsRepository : JpaRepository<AchievementsEntity, Long> {
    @Query("select a from AchievementsEntity a where _uid = ?1 ")
    fun findAllByUid(
        uid: String
    ): List<AchievementsEntity>

    fun findAllByDisponivel(
        available: Boolean
    ): List<AchievementsEntity>
}