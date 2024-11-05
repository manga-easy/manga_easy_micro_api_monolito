package br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.entities.PermissionsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PermissionsRepository : JpaRepository<PermissionsEntity, String> {
    fun findByUserId(userId: String): Optional<PermissionsEntity>

}