package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.FeatureEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.FixEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.ReleaseNotesEntity
import java.util.Date

class UpdateReleaseNotesDto(
    val description: String?,
    val fixes: List<FixEntity> = emptyList(),
    val features: List<FeatureEntity> = emptyList()
)
