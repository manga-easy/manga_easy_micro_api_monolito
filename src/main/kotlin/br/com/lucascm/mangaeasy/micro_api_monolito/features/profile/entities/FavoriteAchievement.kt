package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities

import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity

data class FavoriteAchievement(
    val order: Int = 0,
    val achievement: AchievementsEntity? = null,
)