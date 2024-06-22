package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
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
@RequestMapping("/v1/notifications")
@Deprecated("Remover na 0.18.0 -> 0.20.0")
class NotificationsV1Controller(@Autowired val repository: NotificationsRepository) {
    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var messageService: MessageService

    @GetMapping("/list")
    @ResponseBody
    fun list(
        @RequestParam status: String?,
        @RequestParam idhost: Int?
    ): ResultEntity {
        return try {
            val result: List<NotificationsEntity> = repository.findTop25ByOrderByCreatedAtDesc()
            ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result.map {
                    NotificationV1Dto(
                        image = it.image,
                        createdat = it.createdAt,
                        datemade = it.createdAt,
                        menssege = it.message,
                        titulo = it.title,
                        uid = it.id,
                        updatedat = it.createdAt,
                    )
                }.toList(),
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun delete(
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            repository.deleteById(id)
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(),
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @PostMapping
    @ResponseBody
    fun create(
        @RequestBody body: NotificationV1Dto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        try {
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
            messageService.sendNotification(result)
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Enviada com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }


}