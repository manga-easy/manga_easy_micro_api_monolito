package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos.FoundLikeMangaDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.LikeMangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.LikeMangaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    fun create(@PathVariable uniqueid: String, @AuthenticationPrincipal userAuth: UserAuth): LikeMangaEntity {
        catalogRepository.findByUniqueid(uniqueid)
            ?: throw BusinessException("Manga não encontrado: $uniqueid")
        return likeMangaRepository.findByUniqueidAndUserId(uniqueid, userAuth.userId)
            ?: likeMangaRepository.save(
                LikeMangaEntity(
                    userId = userAuth.userId,
                    uniqueid = uniqueid,
                    createdAt = Date().time,
                )
            )

    }

    @GetMapping("/v1/like")
    fun get(@PathVariable uniqueid: String, @AuthenticationPrincipal userAuth: UserAuth): FoundLikeMangaDto {
        catalogRepository.findByUniqueid(uniqueid)
            ?: throw BusinessException("Manga não encontrado: $uniqueid")
        val result = likeMangaRepository.findByUniqueidAndUserId(uniqueid, userAuth.userId)
        return FoundLikeMangaDto(liked = result != null)
    }

    @DeleteMapping("/v1/like")
    fun delete(@PathVariable uniqueid: String, @AuthenticationPrincipal userAuth: UserAuth) {
        val result = likeMangaRepository.findByUniqueidAndUserId(uniqueid, userAuth.userId)
            ?: throw BusinessException("Like não encontrado")
        likeMangaRepository.deleteById(result.id!!)
    }
}