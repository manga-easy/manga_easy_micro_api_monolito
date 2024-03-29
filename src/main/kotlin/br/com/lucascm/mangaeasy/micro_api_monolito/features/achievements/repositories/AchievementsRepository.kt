package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AchievementsRepository : JpaRepository<AchievementsEntity, Long> {
    fun findByUid(
        uid: String
    ): AchievementsEntity?

    fun findByDisponivelOrderByCreatedatDesc(
        available: Boolean
    ): List<AchievementsEntity>

    @Query(
        """
            SELECT e FROM AchievementsEntity e 
                INNER JOIN UsersAchievementsEntity u 
                ON e.uid = u.idemblema 
            WHERE u.userid = :userId
        """
    )
    fun findByUser(@Param("userId") userId: String): List<AchievementsEntity>
}