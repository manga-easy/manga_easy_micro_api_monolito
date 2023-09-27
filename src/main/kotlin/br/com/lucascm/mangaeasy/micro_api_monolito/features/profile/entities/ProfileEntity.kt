package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities

import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersLevelsEntity
import jakarta.persistence.*

@Entity
@Table(name = "profile")
data class ProfileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,
    var uid: String? = null,
    var role: String? = null,
    var biography: String? = null,
    @Column(name = "user_id", unique = true)
    var userID: String = "",
    @Column(name = "mangas_highlight")
    var mangasHighlight: List<String>? = null,
    @Column(name = "achievements_highlight")
    var achievementsHighlight: List<String>? = null,
    @Column(name = "created_at")
    var createdAt: Long? = null,
    @Column(name = "updated_at")
    var updatedAt: Long? = null,
    @Column(name = "total_manga_read")
    var totalMangaRead: Long? = null,
    @Column(name = "current_level")
    var currentLevel: String? = null,
    @Column(name = "total_achievements")
    var totalAchievements: Long? = null,
)