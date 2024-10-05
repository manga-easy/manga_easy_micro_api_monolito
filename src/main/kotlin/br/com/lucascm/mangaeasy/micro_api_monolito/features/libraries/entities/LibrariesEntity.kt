package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "library")
data class LibrariesEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,

    @Column(name = "host_id", nullable = false)
    val hostId: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: String = "",
    @Column(name = "uniqueid", nullable = false)
    val uniqueId: String = "",
    val manga: String = "",
    val status: String = "",

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Long = 0,

    @Column(name = "created_at", nullable = false)
    val createdAt: Long = 0,

    @Column(name = "is_deleted", nullable = false)
    val hasDeleted: Boolean = false,

    @Column(name = "catalog_id")
    val catalogId: String? = null
)