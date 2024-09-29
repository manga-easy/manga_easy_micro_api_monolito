package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.entities

import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.converters.FeatureEntityConverter
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.converters.FixEntityConverter
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "release-note")
data class ReleaseNoteEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,

    @Column(name = "version", unique = true, nullable = false)
    val version: String = "",

    val description: String? = null,
    val createdAt: Long = 0,

    @Lob
    @Convert(converter = FixEntityConverter::class)
    @Column(name = "release_note_fix", columnDefinition = "json")
    val fixes: List<FixEntity> = listOf(),

    @Lob
    @Convert(converter = FeatureEntityConverter::class)
    @Column(name = "release_note_feature", columnDefinition = "json")
    val features: List<FeatureEntity> = listOf(),
)