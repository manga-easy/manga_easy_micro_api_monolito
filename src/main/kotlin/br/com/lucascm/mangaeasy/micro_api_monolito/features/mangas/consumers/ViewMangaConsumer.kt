package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.consumers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.QueueName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos.ViewMangaConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.ViewMangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.ViewMangaRepository
import com.github.sonus21.rqueue.annotation.RqueueListener
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class ViewMangaConsumer {
    @Autowired
    lateinit var viewMangaRepository: ViewMangaRepository
    val log = KotlinLogging.logger("ViewMangaConsumer")

    @RqueueListener(QueueName.VIEW_MANGA, numRetries = "3")
    fun onMessage(view: ViewMangaConsumerDto) {
        log.info("---------- onMessage init ----------------")
        log.info("---------- onMessage view {} ----------------", view)
        val result = viewMangaRepository.findByUniqueidAndUserId(view.uniqueid, view.userId)
        if (result == null) {
            viewMangaRepository.save(
                ViewMangaEntity(
                    userId = view.uniqueid,
                    uniqueid = view.userId,
                    createdAt = Date().time,
                )
            )
        }
        log.info("---------- onMessage finish ----------------")
    }
}