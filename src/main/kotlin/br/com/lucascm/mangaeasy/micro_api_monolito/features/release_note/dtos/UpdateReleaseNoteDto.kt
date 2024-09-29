package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.entities.FeatureEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.entities.FixEntity

class UpdateReleaseNoteDto(
    val description: String?,
    val fixes: List<FixEntity> = emptyList(),
    val features: List<FeatureEntity> = emptyList()
)
