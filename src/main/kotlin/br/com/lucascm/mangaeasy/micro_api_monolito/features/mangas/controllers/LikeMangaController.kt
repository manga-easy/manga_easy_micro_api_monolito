package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.LikeMangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.LikeMangaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/catalog/{uniqueid}")
class LikeMangaController {
    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var likeMangaRepository: LikeMangaRepository

    @PostMapping("/v1/like")
    @ResponseBody
    fun create(@PathVariable uniqueid: String, authentication: Authentication): ResultEntity {
        return try {
            val userId = authentication.principal.toString()
            catalogRepository.findByUniqueid(uniqueid)
                ?: throw BusinessException("Manga não encontrado: $uniqueid")
            val result = likeMangaRepository.findByUniqueidAndUserId(uniqueid, userId)
            if (result == null) {
                val resultSave = likeMangaRepository.save(
                    LikeMangaEntity(
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

    @GetMapping("/v1/like")
    @ResponseBody
    fun get(@PathVariable uniqueid: String, authentication: Authentication): ResultEntity {
        return try {
            val userId = authentication.principal.toString()
            catalogRepository.findByUniqueid(uniqueid)
                ?: throw BusinessException("Manga não encontrado: $uniqueid")
            val result = likeMangaRepository.findByUniqueidAndUserId(uniqueid, userId)
            ResultEntity(listOf(result != null))
        } catch (e: Exception) {
            HandleExceptions().handleCatch(e)
        }
    }

    @DeleteMapping("/v1/like")
    @ResponseBody
    fun delete(@PathVariable uniqueid: String, authentication: Authentication): ResultEntity {
        return try {
            val userId = authentication.principal.toString()
            val result = likeMangaRepository.findByUniqueidAndUserId(uniqueid, userId)
                ?: throw BusinessException("Like não encontrado")
            val resultSave = likeMangaRepository.deleteById(result.id!!)
            ResultEntity(listOf(resultSave))
        } catch (e: Exception) {
            HandleExceptions().handleCatch(e)
        }
    }
}