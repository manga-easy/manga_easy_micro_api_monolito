package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities

import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsV1Dto

data class FavoriteAchievement(
    val order: Int = 0,
    val achievement: AchievementsV1Dto? = null,
)