package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities

import jakarta.persistence.*


@Entity
@Table(name = "user-achievement")
data class UsersAchievementsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,
    @Column(name = "achievement_id", nullable = false)
    val achievementId: String = "",
    @Column(name = "user_id", nullable = false)
    val userId: String = "",

    @Column(name = "created_at", nullable = false)
    val createdAt: Long = 0,
)