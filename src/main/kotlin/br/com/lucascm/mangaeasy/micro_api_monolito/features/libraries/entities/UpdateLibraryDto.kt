package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities

data class UpdateLibraryDto(
    val hostId: Long = 0,
    val uniqueid: String = "",
    val manga: String = "",
    val status: String = "",
    val hasDeleted: Boolean = false
)