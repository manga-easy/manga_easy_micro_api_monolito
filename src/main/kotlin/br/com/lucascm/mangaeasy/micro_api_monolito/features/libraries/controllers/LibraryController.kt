package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.LibrariesEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.UpdateLibraryDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.repositories.LibrariesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/library")
@Tag(name = "Library")
class LibraryController {
    @Autowired
    lateinit var repository: LibrariesRepository

    @GetMapping("/v1")
    fun list(
        @RequestParam uniqueId: String?,
        @RequestParam limit: Int?,
        @RequestParam offset: Int?,
        @AuthenticationPrincipal userAuth: UserAuth
    ): List<LibrariesEntity> {
        return if (uniqueId != null) {
            repository.findByUserIdAndUniqueid(
                userId = userAuth.userId,
                uniqueid = uniqueId
            )
        } else {
            repository.findByUserId(
                userId = userAuth.userId,
                pageable = PageRequest.of(offset ?: 0, limit ?: 25)
            )
        }
    }

    @PutMapping("/v1")
    fun update(
        @RequestBody body: UpdateLibraryDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): LibrariesEntity {
        val result = repository.findByUserIdAndUniqueid(
            userId = userAuth.userId,
            uniqueid = body.uniqueid
        )
        if (result.isEmpty()) {
            return repository.save(
                LibrariesEntity(
                    createdAt = Date().time,
                    updatedAt = Date().time,
                    userId = userAuth.userId,
                    hostId = body.hostId,
                    hasDeleted = body.hasDeleted,
                    manga = body.manga,
                    uniqueid = body.uniqueid,
                    status = body.status

                )
            )
        }
        val first = result.first()
        return repository.save(
            first.copy(
                updatedAt = Date().time,
                status = body.status,
                hasDeleted = body.hasDeleted,
                manga = body.manga,
                hostId = body.hostId,
                uniqueid = body.uniqueid
            )
        )
    }
}