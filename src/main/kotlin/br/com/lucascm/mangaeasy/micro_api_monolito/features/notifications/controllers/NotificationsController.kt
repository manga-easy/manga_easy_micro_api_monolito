package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.repositories.NotificationsRepository
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/v1/notifications")
class NotificationsController(@Autowired val repository: NotificationsRepository) {
    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/list")
    @ResponseBody
    fun list(
        @RequestParam status: String?,
        @RequestParam idhost: Int?
    ): ResultEntity {
        return try {
            val result: List<NotificationsEntity> = repository.findTop25ByOrderByCreatedatDesc()
            ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @DeleteMapping("/{uid}")
    @ResponseBody
    fun delete(
        @PathVariable uid: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            val result = repository.findByUid(uid)
                ?: throw BusinessException("Notificação não encontrada")
            repository.delete(result)
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
        @RequestBody body: NotificationsEntity,
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            if (body.titulo.isEmpty()) {
                throw BusinessException("O titulo não pode ser vazio")
            }
            sendNotification(body)
            val result = repository.save(
                body.copy(
                    uid = GetUidByFeature().get("notifications"),
                    updatedat = Date().time,
                    createdat = Date().time,
                    datemade = Date().time,
                )
            )
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

    private fun sendNotification(entity: NotificationsEntity) {
        // This registration token comes from the client FCM SDKs.
        val notification = Notification.builder()
            .setTitle(entity.titulo)
            .setBody(entity.menssege)
            .setImage(entity.image)
            .build()
        val message = Message.builder()
            .setTopic("avisos")
            .setNotification(notification)
            .build()

        // Send a message to the device corresponding to the provided
        // registration token.
        val response = FirebaseMessaging.getInstance().send(message)
        // Response is a message ID string.
        print("Successfully sent message: $response")
    }
}