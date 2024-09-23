package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.MessageService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.CreateNotificationDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationStatus
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.repositories.NotificationsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/notifications")
@Tag(name = "Notifications")
class NotificationsController {
    @Autowired
    lateinit var repository: NotificationsRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var messageService: MessageService

    @GetMapping("/v1")
    @ResponseBody
    fun list(): List<NotificationsEntity> {
        return repository.findTop25ByOrderByCreatedAtDesc()
    }

    @GetMapping("/v1/{id}")
    fun getById(@PathVariable id: String): NotificationsEntity {
        return repository.findById(id).getOrNull()
            ?: throw BusinessException("Notification não encontrado")
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
        @RequestBody body: CreateNotificationDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): NotificationsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        if (body.title.isEmpty()) {
            throw BusinessException("O titulo não pode ser vazio")
        }
        val result = repository.save(
            NotificationsEntity(
                createdAt = Date().time,
                status = NotificationStatus.PROCESSING,
                message = body.message,
                title = body.title,
                image = body.image,
            )
        )
        messageService.sendNotification(result)
        return result
    }
}