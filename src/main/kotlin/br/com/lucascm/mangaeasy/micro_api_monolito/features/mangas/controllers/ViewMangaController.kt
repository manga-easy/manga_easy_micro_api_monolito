package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.ViewMangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.ViewMangaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/catalog/{uniqueid}")
class ViewMangaController {
    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var viewMangaRepository: ViewMangaRepository

    @PostMapping("/v1/view")
    fun putView(@PathVariable uniqueid: String, @AuthenticationPrincipal userAuth: UserAuth): ViewMangaEntity {
        catalogRepository.findByUniqueid(uniqueid)
            ?: throw BusinessException("Manga não encontrado: $uniqueid")
        return viewMangaRepository.findByUniqueidAndUserId(uniqueid, userAuth.userId)
            ?: viewMangaRepository.save(
                ViewMangaEntity(
                    userId = userAuth.userId,
                    uniqueid = uniqueid,
                    createdAt = Date().time,
                )
            )

    }
}