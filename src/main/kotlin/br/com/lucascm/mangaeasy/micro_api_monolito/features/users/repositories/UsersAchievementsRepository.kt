package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersAchievementsEntity
import org.springframework.data.jpa.repository.JpaRepository


interface UsersAchievementsRepository : JpaRepository<UsersAchievementsEntity, Long> {
    fun findAllByUserid(
        userId: String
    ): List<UsersAchievementsEntity>

    fun findAllByUseridAndIdemblema(
        userId: String,
        idemblema: String
    ): List<UsersAchievementsEntity>
}