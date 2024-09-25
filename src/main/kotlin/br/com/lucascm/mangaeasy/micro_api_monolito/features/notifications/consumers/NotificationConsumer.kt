package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.consumers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.QueueName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationStatus
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.repositories.NotificationsRepository
import com.github.sonus21.rqueue.annotation.RqueueListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class NotificationConsumer {
    @Autowired
    lateinit var repository: NotificationsRepository
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    @RqueueListener(QueueName.NOTIFICATION, numRetries = "0", concurrency = "1")
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
            log.debug("Successfully sent message: $response")
            repository.save(
                notification.copy(
                    status = NotificationStatus.SUCCESS
                )
            )
        } catch (e: Exception) {
            log.error("Exception", e)
            repository.save(
                notification.copy(
                    status = NotificationStatus.ERROR
                )
            )

        }
        Thread.sleep(Duration.ofSeconds(5).toMillis())
    }
}