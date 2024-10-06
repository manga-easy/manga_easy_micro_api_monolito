package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.services

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.LibrariesEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.UpdateLibraryDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.repositories.LibrariesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class LibraryService {
    @Autowired
    lateinit var librariesRepository: LibrariesRepository
    fun findByUserIdAndUniqueId(userId: String, uniqueId: String): LibrariesEntity? {
        val result = librariesRepository.findByUserIdAndUniqueIdOrderByUpdatedAtDesc(userId, uniqueId)
        if (result.get().isEmpty()) {
            return null
        }
        val libraries = result.get()
        if (libraries.size == 1) return libraries.first()
        val library = libraries.first()
        for (item in libraries.drop(1)) {
            librariesRepository.deleteById(item.id!!)
        }
        return library
    }

    fun findByUserId(userId: String, pageable: Pageable): List<LibrariesEntity> {
        return librariesRepository.findByUserId(userId, pageable)
    }

    fun update(libraryId: String, body: UpdateLibraryDto): LibrariesEntity {
        if (body.uniqueId.isEmpty()) {
            throw BusinessException("UniqueId não pode ser vazio")
        }
        val result = librariesRepository.findById(libraryId).getOrNull()
            ?: throw BusinessException("Manga não encontrado")
        return librariesRepository.save(
            result.copy(
                updatedAt = Date().time,
                hasDeleted = body.hasDeleted,
                manga = body.manga,
                status = body.status,
                hostId = body.hostId
            )
        )
    }

    fun create(userId: String, body: UpdateLibraryDto): LibrariesEntity {
        if (body.uniqueId.isEmpty()) {
            throw BusinessException("UniqueId não pode ser vazio")
        }
        val result = librariesRepository.findByUserIdAndUniqueIdOrderByUpdatedAtDesc(
            userId = userId,
            uniqueId = body.uniqueId
        )
        if (!result.isEmpty) {
            throw BusinessException("Manga já cadastrado")
        }
        return librariesRepository.save(
            LibrariesEntity(
                updatedAt = Date().time,
                createdAt = Date().time,
                userId = userId,
                uniqueId = body.uniqueId,
                manga = body.manga,
                hostId = body.hostId,
                status = body.status,
                hasDeleted = body.hasDeleted
            )
        )
    }
}