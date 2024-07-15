package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities

data class UpdateProfileDto(
    val biography: String? = null,
    val name: String? = null,
    val visibleStatics: Boolean = true,
    val visibleAchievements: Boolean = true,
    val visibleMangas: Boolean = true,
)