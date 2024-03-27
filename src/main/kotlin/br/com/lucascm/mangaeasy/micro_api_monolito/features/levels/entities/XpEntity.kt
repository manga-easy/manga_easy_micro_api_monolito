package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities

import jakarta.persistence.*

@Entity
@Table(name = "xp")
data class XpEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,
    @Column(name = "uniqueid", nullable = false)
    var uniqueID: String = "",
    @Column(name = "chapter_number")
    var chapterNumber: String = "",
    var quantity: Long = 0,
    @Column(name = "total_minutes", nullable = false)
    var totalMinutes: Long = 0,
    @Column(name = "user_id", nullable = false)
    var userID: String = "",
    @Column(name = "created_at")
    var createdAt: Long? = null,
    @Column(name = "updated_at")
    var updatedAt: Long? = null,
)
