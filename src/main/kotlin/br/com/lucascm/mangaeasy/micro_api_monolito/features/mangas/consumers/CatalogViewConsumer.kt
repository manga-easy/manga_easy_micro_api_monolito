package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.consumers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.QueueName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos.CatalogsViewsConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogViewEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogViewRepository
import com.github.sonus21.rqueue.annotation.RqueueListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class CatalogViewConsumer {
    @Autowired
    lateinit var viewMangaRepository: CatalogViewRepository

    @RqueueListener(QueueName.CATALOG_VIEW, numRetries = "1", concurrency = "1")
    fun onMessage(view: CatalogsViewsConsumerDto) {
        val result = viewMangaRepository.findByCatalogIdAndUserId(view.catalogId, view.userId)
        if (result == null) {
            viewMangaRepository.save(
                CatalogViewEntity(
                    userId = view.userId,
                    catalogId = view.catalogId,
                    createdAt = Date().time,
                )
            )
        }
    }
}