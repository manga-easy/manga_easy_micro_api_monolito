package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities

import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsV1Dto

@Deprecated("Remover 0.18 -> 0.20")
data class FavoriteAchievementV1(
    val order: Int = 0,
    val achievement: AchievementsV1Dto? = null,
)