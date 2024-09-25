package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsV1Dto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.services.RecommendationsService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/recommendations")
@Deprecated("Remover 0.18 -> 0.20")
@Tag(name = "Recommendations")
class RecommendationsV1Controller {


    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser


    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var recommendationsService: RecommendationsService

    @GetMapping("/list")
    @ResponseBody
    fun list(
        @RequestParam isAll: Boolean?,
    ): ResultEntity {
        return try {
            val result = recommendationsService.list(0)
            ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result.map {
                    RecommendationsV1Dto(
                        title = it.title,
                        uniqueid = it.uniqueid,
                        updatedat = it.updatedAt,
                        uid = it.id,
                        createdat = it.createdAt,
                        link = it.link,
                        artistid = it.artistId,
                        datacria = it.createdAt,
                        artistname = it.artistName,
                    )
                },
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }


    @GetMapping("/{title}/anilist")
    fun getAnilistRecommendation(
        @PathVariable title: String,
        @AuthenticationPrincipal userAuth: UserAuth,
    ): ResultEntity {
        return try {
            val recommendations = recommendationsService.anilist(title)
            ResultEntity(recommendations)
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }
}