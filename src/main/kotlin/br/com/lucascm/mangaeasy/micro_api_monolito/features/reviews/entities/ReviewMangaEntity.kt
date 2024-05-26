package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities

import jakarta.persistence.*

@Entity
@Table(name = "review-manga")
data class ReviewMangaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val uniqueid: String = "",
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
    val updatedAt: Long? = 0,
    @Column(name = "created_at")
    val createdAt: Long? = 0,
)