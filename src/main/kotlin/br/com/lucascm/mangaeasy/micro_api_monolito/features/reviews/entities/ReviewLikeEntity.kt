package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities

import jakarta.persistence.*

@Entity
@Table(name = "review-like")
data class ReviewLikeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "review_id", nullable = false)
    val reviewId: String = "",
    @Column(name = "user_id", nullable = false)
    val userId: String = "",
    @Column(name = "created_at")
    val createdAt: Long? = 0,
)