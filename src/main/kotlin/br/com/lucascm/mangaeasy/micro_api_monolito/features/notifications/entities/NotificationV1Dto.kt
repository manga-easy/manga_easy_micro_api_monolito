package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities

@Deprecated("Remover na 0.18.0 -> 0.20.0")
data class NotificationV1Dto(
    val uid: String? = null,
    val titulo: String = "",
    val menssege: String? = null,
    val image: String? = null,
    val datemade: Long? = null,
    val createdat: Long? = null,
    val updatedat: Long? = null,
)