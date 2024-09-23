package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos.FoundLikeCatalogDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogLikeEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogLikeRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/catalogs/{catalogId}/likes")
@Tag(name = "Catalogs")
class CatalogsLikesController {
    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var likeMangaRepository: CatalogLikeRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @PostMapping("/v1")
    fun create(@PathVariable catalogId: String, @AuthenticationPrincipal userAuth: UserAuth): CatalogLikeEntity {
        catalogRepository.findById(catalogId).getOrNull()
            ?: throw BusinessException("Manga não encontrado: $catalogId")
        return likeMangaRepository.findByCatalogIdAndUserId(catalogId, userAuth.userId)
            ?: likeMangaRepository.save(
                CatalogLikeEntity(
                    userId = userAuth.userId,
                    catalogId = catalogId,
                    createdAt = Date().time,
                )
            )

    }

    @GetMapping("/v1/users/{userId}")
    fun get(@PathVariable catalogId: String, @PathVariable userId: String): FoundLikeCatalogDto {
        catalogRepository.findById(catalogId).getOrNull()
            ?: throw BusinessException("Manga não encontrado: $catalogId")
        val result = likeMangaRepository.findByCatalogIdAndUserId(catalogId, userId)
        return FoundLikeCatalogDto(liked = result != null)
    }

    @DeleteMapping("/v1/users/{userId}")
    fun delete(
        @PathVariable catalogId: String,
        @PathVariable userId: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ) {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        val result = likeMangaRepository.findByCatalogIdAndUserId(catalogId, userId)
            ?: throw BusinessException("Like não encontrado")
        likeMangaRepository.deleteById(result.id!!)
    }
}