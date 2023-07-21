package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HostsRepository : JpaRepository<HostsEntity, Long> {
    fun findByStatus(
        status: String
    ): List<HostsEntity>

    fun findByIdhost(idhost: Int): List<HostsEntity>
    fun findByUid(uid: String): HostsEntity?
}