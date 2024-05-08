package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.ViewMangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.ViewMangaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/catalog/{uniqueid}")
class ViewMangaController {
    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var viewMangaRepository: ViewMangaRepository

    @PostMapping("/v1/view")
    @ResponseBody
    fun putView(@PathVariable uniqueid: String, authentication: Authentication): ResultEntity {
        return try {
            val userId = authentication.principal.toString()
            catalogRepository.findByUniqueid(uniqueid)
                ?: throw BusinessException("Manga não encontrado: $uniqueid")
            val result = viewMangaRepository.findByUniqueidAndUserId(uniqueid, userId)
            if (result == null) {
                val resultSave = viewMangaRepository.save(
                    ViewMangaEntity(
                        userId = userId,
                        uniqueid = uniqueid,
                        createdAt = Date().time,
                    )
                )
                return ResultEntity(listOf(resultSave))
            }
            ResultEntity(listOf(result))
        } catch (e: Exception) {
            HandleExceptions().handleCatch(e)
        }
    }
}