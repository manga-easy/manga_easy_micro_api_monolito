package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersAchievementsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface UsersAchievementsRepository : JpaRepository<UsersAchievementsEntity, Long> {
    fun findAllByUserid(
        userId: String
    ): List<UsersAchievementsEntity>

    fun findAllByUseridAndIdemblema(
        userId: String,
        idemblema: String
    ): List<UsersAchievementsEntity>
    @Query("""
        SELECT COUNT(*)
        FROM UsersAchievementsEntity dc 
        WHERE dc.userid = :userId
    """)
    fun countByUserId(@Param("userId") userId: String): Long
}