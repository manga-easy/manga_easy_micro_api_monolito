package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.FeatureEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.FixEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.ReleaseNotesEntity
import java.util.Date

class ReleaseNotesDto(
    val version: String,
    val description: String?,
    val fixes: List<FixEntity> = emptyList(),
    val features: List<FeatureEntity> = emptyList()
) {
    fun toEntity(): ReleaseNotesEntity {
        return ReleaseNotesEntity(
            version = this.version,
            description = this.description,
            features = this.features,
            fixes = this.fixes,
            createdAt = Date().time
        )
    }
}
