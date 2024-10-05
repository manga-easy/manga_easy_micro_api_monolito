package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.LibrariesEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.UpdateLibraryDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.services.LibraryService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/{userId}/libraries")
@Tag(name = "Libraries")
class LibraryController {
    @Autowired
    lateinit var libraryService: LibraryService

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
        return libraryService.findByUserId(
            userId = userId,
            pageable = PageRequest.of(offset ?: 0, limit ?: 25)
        )
    }

    @GetMapping("/v1/manga/{uniqueId}")
    fun getByUniqueId(
        @PathVariable uniqueId: String,
        @PathVariable userId: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ): LibrariesEntity? {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        return libraryService.findByUserIdAndUniqueId(
            userId = userId,
            uniqueId = uniqueId
        )
    }

    @PutMapping("/v1/{id}")
    fun update(
        @PathVariable userId: String,
        @PathVariable id: String,
        @RequestBody body: UpdateLibraryDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): LibrariesEntity {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        return libraryService.update(id, body)
    }

    @PostMapping("/v1")
    fun create(
        @PathVariable userId: String,
        @RequestBody body: UpdateLibraryDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): LibrariesEntity {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        return libraryService.create(userId, body)
    }
}