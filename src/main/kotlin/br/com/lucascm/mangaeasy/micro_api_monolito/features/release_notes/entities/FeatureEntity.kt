package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "release-notes-feature")
data class FeatureEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,

    val title: String = "",
    val subtitle: String? = null,
    val description: String? = null,

    @ManyToOne
    @JoinColumn(name = "release_notes_id")
    val releaseNotes: ReleaseNotesEntity? = null
)
