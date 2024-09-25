package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoryEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.UpdateHistoryDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.repositories.HistoriesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/users/{userId}/histories")
@Tag(name = "Histories")
class HistoryController {
    @Autowired
    lateinit var repository: HistoriesRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
    fun list(
        @PathVariable userId: String,
        @RequestParam limit: Int?,
        @RequestParam offset: Int?,
        @AuthenticationPrincipal userAuth: UserAuth
    ): List<HistoryEntity> {
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
    ): HistoryEntity {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        return repository.findByUserIdAndUniqueId(
            userId = userId,
            uniqueid = uniqueId
        ) ?: throw BusinessException("Manga não encontrado")
    }

    @PutMapping("/v1/{historyId}")
    fun update(
        @PathVariable userId: String,
        @PathVariable historyId: String,
        @RequestBody body: UpdateHistoryDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): HistoryEntity {
        if (body.uniqueId.isEmpty()) {
            throw BusinessException("UniqueId não pode ser vazio")
        }
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        val result = repository.findById(historyId).getOrNull()
            ?: throw BusinessException("Manga não encontrado")
        return repository.save(
            result.copy(
                updatedAt = Date().time,
                chaptersRead = body.chaptersRead,
                currentChapter = body.currentChapter,
                isDeleted = body.hasDeleted,
                manga = body.manga,
                uniqueId = body.uniqueId
            )
        )
    }

    @PostMapping("/v1")
    fun create(
        @PathVariable userId: String,
        @RequestBody body: UpdateHistoryDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): HistoryEntity {
        if (body.uniqueId.isEmpty()) {
            throw BusinessException("UniqueId não pode ser vazio")
        }
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        val result = repository.findByUserIdAndUniqueId(
            userId = userId,
            uniqueid = body.uniqueId
        )
        if (result != null) {
            throw BusinessException("Manga já cadastrado")
        }
        return repository.save(
            HistoryEntity(
                updatedAt = Date().time,
                createdAt = Date().time,
                userId = userAuth.userId,
                uniqueId = body.uniqueId,
                chaptersRead = body.chaptersRead,
                currentChapter = body.currentChapter,
                isDeleted = body.hasDeleted,
                manga = body.manga,
                catalogId = null,
            )
        )
    }
}