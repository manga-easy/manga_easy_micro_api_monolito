package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities

class UserEntity(
    val uid: String? = null,
    val name: String = "",
    val registration: String = "",
    val status: Boolean = false,
    val email: String = "",
    val emailverification: Boolean = false,
    val prefs: String = "",
)