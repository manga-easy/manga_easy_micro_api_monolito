package br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages

import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos.CatalogsViewsConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationsEntity
import com.github.sonus21.rqueue.core.RqueueMessageEnqueuer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class MessageService {
    @Autowired
    lateinit var rqueueMessageEnqueuer: RqueueMessageEnqueuer

    fun sendXp(xp: XpConsumerDto) {
        rqueueMessageEnqueuer.enqueue(QueueName.XP, xp)
    }

    fun sendViewManga(view: CatalogsViewsConsumerDto) {
        rqueueMessageEnqueuer.enqueue(QueueName.CATALOG_VIEW, view)
    }

    fun sendNotification(notification: NotificationsEntity) {
        rqueueMessageEnqueuer.enqueue(QueueName.NOTIFICATION, notification)
    }
}