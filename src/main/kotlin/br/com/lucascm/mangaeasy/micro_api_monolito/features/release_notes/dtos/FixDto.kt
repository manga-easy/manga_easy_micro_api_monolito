package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.dtos

data class FixDto(
    val title: String,
    val subtitle: String?,
    val description: String?
)