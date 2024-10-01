package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.entities.FeatureEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.entities.FixEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.entities.ReleaseNoteEntity
import java.util.Date

class ReleaseNoteDto(
    val version: String,
    val description: String?,
    val fixes: List<FixEntity> = emptyList(),
    val features: List<FeatureEntity> = emptyList()
) {
    fun toEntity(): ReleaseNoteEntity {
        return ReleaseNoteEntity(
            version = this.version,
            description = this.description,
            features = this.features,
            fixes = this.fixes,
            createdAt = Date().time
        )
    }
}
