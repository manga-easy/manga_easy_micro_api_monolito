package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

import jakarta.persistence.*

@Entity
@Table(name = "catalog")
data class CatalogEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null,
    val uid: String? = null,
    val name: String = "",
    val uniqueid: String = "",
    @Column(name = "last_chapter")
    val lastChapter: String = "",
    @Column(name = "total_views")
    val totalViews: Long = 0,
    val author: String = "",
    val thumb: String = "",
    val synopsis: String = "",
    val ratio: Double = 0.0,
    val scans: String = "",
    @Column(name = "updated_at")
    val updatedAt: Long = 0,
    @Column(name = "created_at")
    val createdAt: Long = 0,
    val genres: String = "",
    val year: Long? = null
)