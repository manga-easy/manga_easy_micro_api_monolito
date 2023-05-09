package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersLevelsEntity
import org.springframework.data.jpa.repository.JpaRepository


interface UsersLevelsRepository : JpaRepository<UsersLevelsEntity, Long> {
    fun findByTemporadaAndUserid(
       idSeason: String,
       userID: String
    ): List<UsersLevelsEntity>

}