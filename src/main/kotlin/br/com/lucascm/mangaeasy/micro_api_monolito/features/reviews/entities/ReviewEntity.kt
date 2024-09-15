package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "review")
data class ReviewEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    @Column(nullable = false, name = "catalog_id")
    val catalogId: String = "",
    @Column(nullable = false, name = "user_id")
    val userId: String = "",
    val commentary: String? = null,
    val totalLikes: Long = 0,
    @Column(nullable = false, name = "is_spoiler")
    val hasSpoiler: Boolean = false,
    @Column(nullable = false, name = "is_updated")
    val hasUpdated: Boolean = false,
    val rating: Double = 0.0,
    @Column(name = "updated_at")
    val updatedAt: Long = 0,
    @Column(name = "created_at")
    val createdAt: Long = 0,
)