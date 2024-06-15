package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpConsumerDto
import com.github.sonus21.rqueue.core.RqueueMessageEnqueuer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class MessageService {
    @Autowired
    lateinit var rqueueMessageEnqueuer: RqueueMessageEnqueuer

    // send notification in 30 seconds
    fun sendNotification(xp: XpConsumerDto) {
        rqueueMessageEnqueuer.enqueue("xp", xp)
    }
}