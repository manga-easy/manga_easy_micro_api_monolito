package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "achievement")
data class AchievementsEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    val name: String = "",
    val rarity: String = "",
    val description: String = "",
    @Column(name = "rarity_percent", nullable = false)
    val percentRarity: Double = 0.0,
    @Column(name = "total_acquired", nullable = false)
    val totalAcquired: Long = 0,
    val url: String = "",
    val benefits: String = "",
    val reclaim: Boolean = false,
    val category: String = "",
    @Column(name = "created_at", nullable = false)
    val createdAt: Long = 0,
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Long = 0
)