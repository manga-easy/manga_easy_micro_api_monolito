package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "catalog")
data class CatalogEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    val name: String = "",
    val uniqueid: String = "",
    @Column(name = "last_chapter")
    val lastChapter: String? = null,
    @Column(name = "total_views", nullable = false)
    val totalViews: Long = 0,
    @Column(name = "total_likes", nullable = false)
    val totalLikes: Long = 0,
    @Column(name = "total_comments", nullable = false)
    val totalComments: Long = 0,
    val author: String = "",
    val thumb: String = "",
    val synopsis: String = "",
    val ratio: Double = 0.0,
    val scans: String = "",
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Long = 0,
    @Column(name = "created_at", nullable = false)
    val createdAt: Long = 0,
    val genres: String = "",
    val year: Long? = null,
)