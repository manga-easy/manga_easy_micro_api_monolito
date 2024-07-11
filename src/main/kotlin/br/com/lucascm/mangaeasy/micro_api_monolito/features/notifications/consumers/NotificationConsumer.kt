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
import org.springframework.stereotype.Component

@Component
class NotificationConsumer {
    @Autowired
    lateinit var repository: NotificationsRepository
    private val log = KotlinLogging.logger("NotificationConsumer")

    @RqueueListener(QueueName.NOTIFICATION, numRetries = "0")
    fun onMessage(notification: NotificationsEntity) {
        try {
            // This registration token comes from the client FCM SDKs.
            val notificationBuilder = Notification.builder()
                .setTitle(notification.title)
                .setBody(notification.message)
                .setImage(notification.image)
                .build()
            val message = Message.builder()
                .setNotification(notificationBuilder)
                .setTopic("avisos")
                .build()
            // Send a message to the device corresponding to the provided
            // registration token.
            val response = FirebaseMessaging.getInstance().send(message)

            // Response is a message ID string.
            log.info("Successfully sent message: $response")
            repository.save(
                notification.copy(
                    status = NotificationStatus.SUCCESS
                )
            )
        } catch (e: Exception) {
            log.catching(e)

            repository.save(
                notification.copy(
                    status = NotificationStatus.ERROR
                )
            )

        }
    }
}