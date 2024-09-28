package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "release-notes")
data class ReleaseNotesEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,

    @Column(name = "version", unique = true, nullable = false)
    val version: String = "",

    val description: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Long = 0,

    @OneToMany(mappedBy = "releaseNotes", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val fixes: MutableList<FixEntity> = mutableListOf(),

    @OneToMany(mappedBy = "releaseNotes", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val features: MutableList<FeatureEntity> = mutableListOf()
)