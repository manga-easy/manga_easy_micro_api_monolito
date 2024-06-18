package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.MessageService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos.ViewMangaConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/catalog/{uniqueid}")
class ViewMangaController {
    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var messageService: MessageService


    @PostMapping("/v1/view")
    fun putView(@PathVariable uniqueid: String, @AuthenticationPrincipal userAuth: UserAuth) {
        catalogRepository.findByUniqueid(uniqueid)
            ?: throw BusinessException("Manga não encontrado: $uniqueid")
        messageService.sendViewManga(
            ViewMangaConsumerDto(
                uniqueid = uniqueid,
                userId = userAuth.userId
            )
        )
    }
}