package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface HostsRepository :
    JpaSpecificationExecutor<HostsEntity>,
    JpaRepository<HostsEntity, String> {
    fun findByStatus(
        status: String
    ): List<HostsEntity>

    fun findByHostId(hostId: Int): List<HostsEntity>
}