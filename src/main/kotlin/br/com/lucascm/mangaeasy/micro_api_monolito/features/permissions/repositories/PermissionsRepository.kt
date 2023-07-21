package br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.entities.PermissionsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PermissionsRepository : JpaRepository<PermissionsEntity, Long> {
    fun  findByUserid(userID: String): PermissionsEntity?
    fun findByUid(uid: String): PermissionsEntity?

}