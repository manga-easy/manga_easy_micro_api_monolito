package br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "staff")
data class StaffEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,

    @Column(name = "created_at")
    val createdAt: Long = 0,

    @Column(name = "updated_at")
    val updatedAt: Long = 0,

    @Column(name = "type")
    val type: StaffType = StaffType.Designer,

    @Column(name = "user_id", nullable = false, unique = true)
    val userId: String = ""
)

