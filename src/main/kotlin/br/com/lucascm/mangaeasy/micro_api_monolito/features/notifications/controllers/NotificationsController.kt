package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.MessageService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationStatus
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationV1Dto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.repositories.NotificationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/notification")

class NotificationsController {
    @Autowired
    lateinit var repository: NotificationsRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var messageService: MessageService

    @GetMapping("/v1")
    @ResponseBody
    fun list(
        @RequestParam status: String?,
        @RequestParam idhost: Int?
    ): List<NotificationsEntity> {
        return repository.findTop25ByOrderByCreatedAtDesc()
    }

    @DeleteMapping("/v1/{id}")
    @ResponseBody
    fun delete(
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ) {
        handlerPermissionUser.handleIsAdmin(userAuth)
        return repository.deleteById(id)
    }

    @PostMapping("/v1")
    fun create(
        @RequestBody body: NotificationV1Dto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): NotificationsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        if (body.titulo.isEmpty()) {
            throw BusinessException("O titulo não pode ser vazio")
        }
        val result = repository.save(
            NotificationsEntity(
                createdAt = Date().time,
                status = NotificationStatus.PROCESSING,
                message = body.menssege,
                title = body.titulo,
                image = body.image,
            )
        )
        messageService.sendNotification(result.id!!)
        return result
    }
}