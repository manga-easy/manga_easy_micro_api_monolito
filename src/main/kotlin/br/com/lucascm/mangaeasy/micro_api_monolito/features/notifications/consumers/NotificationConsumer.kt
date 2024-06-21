package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.consumers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.QueueName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationStatus
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.repositories.NotificationsRepository
import com.github.sonus21.rqueue.annotation.RqueueListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired

class NotificationConsumer {
    @Autowired
    lateinit var repository: NotificationsRepository
    private val log = KotlinLogging.logger("NotificationConsumer")

    @RqueueListener(QueueName.NOTIFICATION, numRetries = "0")
    fun onMessage(id: String) {
        var entity: NotificationsEntity? = null
        try {
            // This registration token comes from the client FCM SDKs.
            entity = repository.findById(id).get()
            val notification = Notification.builder()
                .setTitle(entity.title)
                .setBody(entity.message)
                .setImage(entity.image)
                .build()
            val message = Message.builder()
                .setNotification(notification)
                .build()
            // Send a message to the device corresponding to the provided
            // registration token.
            val response = FirebaseMessaging.getInstance().send(message)

            // Response is a message ID string.
            log.info("Successfully sent message: $response")
            repository.save(
                entity.copy(
                    status = NotificationStatus.SUCCESS
                )
            )
        } catch (e: Exception) {
            log.catching(e)
            if (entity != null) {
                repository.save(
                    entity.copy(
                        status = NotificationStatus.ERROR
                    )
                )
            }
        }
    }
}