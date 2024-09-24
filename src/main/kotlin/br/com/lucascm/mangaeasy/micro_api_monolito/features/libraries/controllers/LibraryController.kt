package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
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
@RequestMapping("/users/{userId}/libraries")
@Tag(name = "Libraries")
class LibraryController {
    @Autowired
    lateinit var repository: LibrariesRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
    fun list(
        @PathVariable userId: String,
        @RequestParam limit: Int?,
        @RequestParam offset: Int?,
        @AuthenticationPrincipal userAuth: UserAuth
    ): List<LibrariesEntity> {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        return repository.findByUserId(
            userId = userId,
            pageable = PageRequest.of(offset ?: 0, limit ?: 25)
        )
    }

    @GetMapping("/v1/{uniqueId}")
    fun getByUniqueId(
        @PathVariable uniqueId: String,
        @PathVariable userId: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ): LibrariesEntity {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        return repository.findByUserIdAndUniqueid(
            userId = userId,
            uniqueid = uniqueId
        ) ?: throw BusinessException("Manga não encontrado")
    }

    @PutMapping("/v1/{uniqueId}")
    fun update(
        @PathVariable userId: String,
        @PathVariable uniqueId: String,
        @RequestBody body: UpdateLibraryDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): LibrariesEntity {
        if (body.uniqueId.isEmpty()) {
            throw BusinessException("UniqueId não pode ser vazio")
        }
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        val result = repository.findByUserIdAndUniqueid(
            userId = userId,
            uniqueid = uniqueId
        ) ?: throw BusinessException("Manga não encontrado")
        return repository.save(
            result.copy(
                updatedAt = Date().time,
                uniqueid = result.uniqueid,
                hasDeleted = body.hasDeleted,
                manga = body.manga,
                status = body.status,
                hostId = body.hostId,
                userId = userId
            )
        )
    }

    @PostMapping("/v1")
    fun create(
        @PathVariable userId: String,
        @RequestBody body: UpdateLibraryDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): LibrariesEntity {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        val result = repository.findByUserIdAndUniqueid(
            userId = userId,
            uniqueid = body.uniqueId
        )
        if (result != null) {
            throw BusinessException("Manga já cadastrado")
        }
        return repository.save(
            LibrariesEntity(
                updatedAt = Date().time,
                createdAt = Date().time,
                userId = userId,
                uniqueid = body.uniqueId,
                manga = body.manga,
                hostId = body.hostId,
                status = body.status,
                hasDeleted = body.hasDeleted
            )
        )
    }
}