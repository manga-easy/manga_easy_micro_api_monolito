package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("profile")
data class ProfileV1Entity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: ObjectId? = null,
    var role: String = "",
    var biography: String? = null,
    @Column(name = "user_id", unique = true)
    @Indexed(unique = true)
    var userID: String = "",
    @Column(name = "mangas_highlight")
    var mangasHighlight: List<FavoriteManga> = listOf(),
    @Column(name = "achievements_highlight")
    var achievementsHighlight: List<FavoriteAchievement> = listOf(),
    @Column(name = "created_at")
    var createdAt: Long? = null,
    @Column(name = "updated_at")
    var updatedAt: Long? = null,
    @Column(name = "total_manga_read")
    var totalMangaRead: Long = 0,
    @Column(name = "current_level")
    var totalXp: Long = 0,
    @Column(name = "total_achievements")
    var totalAchievements: Long = 0,
    var picture: String? = null,
    var name: String? = null,
    var visibleStatics: Boolean = true,
    var visibleAchievements: Boolean = true,
    var visibleMangas: Boolean = true,
    var isV2: Boolean = false
)