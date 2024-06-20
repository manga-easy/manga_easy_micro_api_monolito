package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities

data class RankingDto(
    val id: String? = null,//É o userId
    val totalXp: Long = 0,
    val place: Long = 0,
    val picture: String? = null,
    val name: String? = null,
)