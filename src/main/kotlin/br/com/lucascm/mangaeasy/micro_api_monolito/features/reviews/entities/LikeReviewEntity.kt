package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities

import jakarta.persistence.*

@Entity
@Table(name = "like-review")
data class LikeReviewEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "review_id", nullable = false)
    val reviewId: Long = 0,
    @Column(name = "user_id", nullable = false)
    val userId: String = "",
    @Column(name = "created_at")
    val createdAt: Long? = 0,
)