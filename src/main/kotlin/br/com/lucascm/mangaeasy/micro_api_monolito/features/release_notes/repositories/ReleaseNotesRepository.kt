package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.ReleaseNotesEntity
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Pageable

interface ReleaseNotesRepository : JpaRepository<ReleaseNotesEntity, String> {
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): List<ReleaseNotesEntity>
    fun findByVersion(version: String): ReleaseNotesEntity?
}