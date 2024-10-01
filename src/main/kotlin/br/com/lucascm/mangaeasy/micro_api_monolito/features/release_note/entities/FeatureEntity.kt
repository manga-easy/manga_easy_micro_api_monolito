package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.entities

data class FeatureEntity(
    val title: String = "",
    val subtitle: String? = null,
    val description: String? = null,
)
