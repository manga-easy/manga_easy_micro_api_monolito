package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.FeatureEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.FixEntity

class UpdateReleaseNotesDto(
    val description: String?,
    val fixes: List<FixEntity> = emptyList(),
    val features: List<FeatureEntity> = emptyList()
)
