package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoryEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.UpdateHistoryDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.services.HistoryService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/{userId}/histories")
@Tag(name = "Histories")
class HistoryController {
    @Autowired
    lateinit var historyService: HistoryService

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
        return historyService.findByUserId(
            userId = userId,
            pageable = PageRequest.of(offset ?: 0, limit ?: 25)
        )
    }

    @GetMapping("/v1/{uniqueId}")
    fun getByUniqueId(
        @PathVariable uniqueId: String,
        @PathVariable userId: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ): HistoryEntity? {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        return historyService.findByUserIdAndUniqueId(
            userId = userId,
            uniqueId = uniqueId
        )
    }

    @PutMapping("/v1/{historyId}")
    fun update(
        @PathVariable userId: String,
        @PathVariable historyId: String,
        @RequestBody body: UpdateHistoryDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): HistoryEntity {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        return historyService.update(historyId, body)
    }

    @PostMapping("/v1")
    fun create(
        @PathVariable userId: String,
        @RequestBody body: UpdateHistoryDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): HistoryEntity {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        return historyService.create(userId, body)
    }
}