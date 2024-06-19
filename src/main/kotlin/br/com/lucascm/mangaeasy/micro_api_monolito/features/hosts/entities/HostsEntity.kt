package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "host")
data class HostsEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val uid: String? = null,
    @Column(name = "host_id", nullable = false)
    val hostId: Int = 0,
    @Column(name = "`order`")
    val order: Int = 0,
    val name: String = "",
    @Column(name = "url_api", nullable = false)
    val urlApi: String = "",
    val status: String = "",
    @Column(name = "created_at", nullable = false)
    val createdAt: Long = 0,
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Long = 0,
)