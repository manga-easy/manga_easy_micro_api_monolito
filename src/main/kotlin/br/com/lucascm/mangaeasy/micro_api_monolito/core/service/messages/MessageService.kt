package br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages

import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos.ViewMangaConsumerDto
import com.github.sonus21.rqueue.core.RqueueMessageEnqueuer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class MessageService {
    @Autowired
    lateinit var rqueueMessageEnqueuer: RqueueMessageEnqueuer

    fun sendXp(xp: XpConsumerDto) {
        rqueueMessageEnqueuer.enqueueIn(QueueName.XP, xp, Duration.ofSeconds(5))
    }

    fun sendViewManga(view: ViewMangaConsumerDto) {
        rqueueMessageEnqueuer.enqueueIn(QueueName.VIEW_MANGA, view, Duration.ofSeconds(5))
    }

    fun sendNotification(id: String) {
        rqueueMessageEnqueuer.enqueueIn(QueueName.NOTIFICATION, id, Duration.ofSeconds(5))
    }
}