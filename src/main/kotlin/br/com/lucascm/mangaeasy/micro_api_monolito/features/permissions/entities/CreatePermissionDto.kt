package br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.entities

data class CreatePermissionDto(
    val userid: String,
    val value: Int
)