package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("profile")
data class ProfileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: ObjectId? = null,
    var role: String = "",
    var biography: String? = null,
    @Indexed(unique = true)
    var userID: String = "",
    var mangasHighlight: List<FavoriteManga> = listOf(),
    var achievementsHighlight: List<FavoriteAchievement> = listOf(),
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var totalMangaRead: Long = 0,
    var totalXp: Long = 0,
    var totalAchievements: Long = 0,
    var picture: String? = null,
    var name: String? = null,
    var visibleStatics: Boolean = true,
    var visibleAchievements: Boolean = true,
    var visibleMangas: Boolean = true,
)