package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities

data class FeatureEntity(
    val title: String = "",
    val subtitle: String? = null,
    val description: String? = null,
)
