package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AchievementsRepository : JpaRepository<AchievementsEntity, String> {
    fun findByReclaimOrderByCreatedAtDesc(
        available: Boolean
    ): List<AchievementsEntity>

    @Query(
        """
            SELECT e FROM AchievementsEntity e 
                INNER JOIN UsersAchievementsEntity u 
                ON e.id = u.achievementId 
            WHERE u.userId = :userId
        """
    )
    fun findByUser(@Param("userId") userId: String): List<AchievementsEntity>
}