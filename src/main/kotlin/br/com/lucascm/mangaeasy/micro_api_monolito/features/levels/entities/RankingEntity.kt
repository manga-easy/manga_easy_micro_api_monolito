package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "ranking")
data class RankingEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    @Column(name = "total_xp", nullable = false)
    var totalXp: Long = 0,
    @Column(name = "user_id", unique = true, nullable = false)
    var userId: String = "",
    var place: Long = 0,
    var picture: String? = null,
    var name: String? = null,
)