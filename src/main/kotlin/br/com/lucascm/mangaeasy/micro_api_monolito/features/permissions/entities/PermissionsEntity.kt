package br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "permission")
data class PermissionsEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    var id: String? = null,
    @Column(name = "user_id", nullable = false)
    var userId: String = "",
    var level: Int = 0,

    @Column(name = "created_at", nullable = false)
    var createdAt: Long = 0,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Long = 0,
)