package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.LibrariesEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LibrariesRepository : JpaRepository<LibrariesEntity, Long> {

    fun findByIduserAndUniqueid(iduser: String, uniqueid: String): List<LibrariesEntity>

    fun findByIduser(iduser: String, pageable: Pageable): List<LibrariesEntity>
}