package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

import jakarta.persistence.*

@Entity
@Table(name = "catalog-view")
data class CatalogViewEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, name = "catalog_id")
    val catalogId: String = "",
    @Column(name = "user_id", nullable = false)
    val userId: String = "",
    @Column(name = "created_at", nullable = false)
    val createdAt: Long = 0,
)