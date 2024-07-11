package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities

data class CreateUserAchievementDto(
    val achievementId: String = "",
    val userId: String = "",
)