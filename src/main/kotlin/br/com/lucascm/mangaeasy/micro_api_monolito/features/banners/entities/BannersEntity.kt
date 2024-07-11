package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "banner")
data class BannersEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    val image: String = "",
    val link: String = "",

    @Column(name = "created_at", nullable = false)
    val createdAt: Long = 0,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Long = 0,
)