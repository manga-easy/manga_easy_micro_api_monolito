package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

import jakarta.persistence.*

@Entity
@Table(name = "catalog")
data class CatalogEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null,
    val uid: String?,
    val name: String,
    val uniqueid: String,
    @Column(name = "last_chapter")
    val lastChapter: String,
    @Column(name = "total_views")
    val totalViews: Long,
    val author: String,
    val thumb: String,
    val synopsis: String,
    val ratio: Float,
    val scans: String,
    @Column(name = "updated_at")
    val updatedAt: Long,
    @Column(name = "created_at")
    val createdAt: Long,
    val genres: String,
    val year: Long?
)