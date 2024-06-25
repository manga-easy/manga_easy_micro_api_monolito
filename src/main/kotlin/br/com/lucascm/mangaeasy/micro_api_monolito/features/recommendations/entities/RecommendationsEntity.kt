package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "recommendation")
data class RecommendationsEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    val uniqueid: String = "",
    val title: String = "",
    val link: String = "",
    @Column(name = "artist_id")
    val artistId: String? = null,
    var artistName: String? = null,
    @Column(name = "created_at")
    val createdAt: Long = 0,
    @Column(name = "updated_at")
    val updatedAt: Long = 0
)