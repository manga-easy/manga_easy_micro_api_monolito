package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.entities.ReleaseNoteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Pageable

interface ReleaseNoteRepository : JpaRepository<ReleaseNoteEntity, String> {
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): List<ReleaseNoteEntity>
    fun findByVersion(version: String): ReleaseNoteEntity?
}