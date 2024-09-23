package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities


import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsV1Dto

@Deprecated("Remover 0.18 -> 0.20")
data class ProfileV1Dto(
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
) {
    companion object {
        fun fromEntity(entity: ProfileEntity): ProfileV1Dto {
            return ProfileV1Dto(
                createdAt = entity.createdAt,
                name = entity.name,
                updatedAt = entity.updatedAt,
                picture = entity.picture,
                totalXp = entity.totalXp,
                visibleMangas = entity.visibleMangas,
                userID = entity.userId,
                visibleAchievements = entity.visibleAchievements,
                achievementsHighlight = entity.achievementsHighlight.map {
                    FavoriteAchievementV1(
                        it.order,
                        if (it.achievement != null) AchievementsV1Dto.fromEntity(it.achievement) else null
                    )
                }.toList(),
                visibleStatics = entity.visibleStatics,
                biography = entity.biography,
                mangasHighlight = entity.mangasHighlight,
                role = entity.role,
                totalAchievements = entity.totalAchievements,
                totalMangaRead = entity.totalMangaRead
            )
        }
    }
}