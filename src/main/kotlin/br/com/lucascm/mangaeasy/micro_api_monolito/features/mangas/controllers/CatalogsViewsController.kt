package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.MessageService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos.CatalogsViewsConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/catalogs/views/manga/{uniqueId}")
@Tag(name = "Catalogs")
class CatalogsViewsController {
    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var messageService: MessageService

    @PostMapping("/v1")
    fun putView(
        @PathVariable uniqueId: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ) {
        val catalog = catalogRepository.findByUniqueid(uniqueId)
            ?: throw BusinessException("Manga não encontrado: $uniqueId")
        messageService.sendViewManga(
            CatalogsViewsConsumerDto(
                catalogId = catalog.id!!,
                userId = userAuth.userId
            )
        )
    }
}