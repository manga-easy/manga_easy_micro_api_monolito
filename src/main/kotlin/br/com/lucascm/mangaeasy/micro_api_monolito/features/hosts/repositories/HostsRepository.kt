package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface HostsRepository : JpaRepository<HostsEntity, Long> {

    @Query("select a from HostsEntity a where status = ?1 ")
    fun findAllByStatus(
        status: String
    ): List<HostsEntity>

    @Query("select a from HostsEntity a where idhost = ?1 ")
    fun findAllByIdhost( idhost: Int): List<HostsEntity>
}