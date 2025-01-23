package br.com.lucascm.mangaeasy.micro_api_monolito.features.catalogs.consumers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.QueueName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.catalogs.dtos.CatalogsViewsConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.catalogs.entities.CatalogViewEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.catalogs.repositories.CatalogViewRepository
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