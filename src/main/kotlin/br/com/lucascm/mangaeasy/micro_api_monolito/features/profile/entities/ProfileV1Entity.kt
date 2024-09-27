package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Deprecated(
    "Use ProfileEntity, remover 0.18 -> 0.20",
    ReplaceWith("ProfileEntity")
)
@Document("profile")
data class ProfileV1Entity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var _id: ObjectId? = null,
    var role: String = "",
    var biography: String? = null,
    var userID: String = "",
    var mangasHighlight: List<FavoriteManga> = listOf(),
    var achievementsHighlight: List<FavoriteAchievementV1> = listOf(),
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
    var isV2: Boolean = false
) {
    fun toV2(): ProfileEntity {
        return ProfileEntity(
            id = null,
            role = role,
            biography = biography,
            userId = userID,
            name = name,
            updatedAt = updatedAt ?: 0,
            createdAt = createdAt ?: 0,
            picture = picture,
            totalXp = totalXp,
            visibleMangas = visibleMangas,
            visibleAchievements = visibleAchievements,
            achievementsHighlight = achievementsHighlight.map {
                FavoriteAchievement(
                    order = it.order,
                    achievement = it.achievement?.toEntity()
                )
            }.toList(),
            totalAchievements = totalAchievements,
            visibleStatics = visibleStatics,
            mangasHighlight = mangasHighlight,
            totalMangaRead = totalMangaRead
        )
    }
}