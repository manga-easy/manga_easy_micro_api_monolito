package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities

@Deprecated("Remover 0.18 -> 0.20")
data class RecommendationsV1Dto(
    val uid: String? = null,
    val uniqueid: String = "",
    val title: String = "",
    val link: String = "",
    val artistid: String? = null,
    val artistname: String? = null,
    val datacria: Long? = null,
    val createdat: Long? = null,
    val updatedat: Long? = null
)