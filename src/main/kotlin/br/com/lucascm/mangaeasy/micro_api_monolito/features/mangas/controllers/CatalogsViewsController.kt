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
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/catalogs/{catalogId}/views")
@Tag(name = "Catalogs")
class CatalogsViewsController {
    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var messageService: MessageService

    @PostMapping("/v1")
    fun putView(
        @PathVariable catalogId: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ) {
        catalogRepository.findById(catalogId).getOrNull()
            ?: throw BusinessException("Manga não encontrado: $catalogId")
        messageService.sendViewManga(
            CatalogsViewsConsumerDto(
                catalogId = catalogId,
                userId = userAuth.userId
            )
        )
    }
}