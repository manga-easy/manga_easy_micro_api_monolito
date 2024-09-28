package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.dtos

data class ReleaseNotesDto(
    val version: String,
    val description: String?,
    val fixes: List<FixDto> = emptyList(),
    val features: List<FeatureDto> = emptyList()
)
