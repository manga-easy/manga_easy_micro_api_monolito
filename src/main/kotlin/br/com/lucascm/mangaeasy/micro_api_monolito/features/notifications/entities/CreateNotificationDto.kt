package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities

data class CreateNotificationDto(
    val title: String = "",
    val message: String? = null,
    val image: String? = null,
)