package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities

@Deprecated("Remover na 0.18.0 -> 0.20.0")
data class RankingV1Dto(
    val id: String? = null,//É o userId
    val totalXp: Long = 0,
    val place: Long = 0,
    val picture: String? = null,
    val name: String? = null,
)