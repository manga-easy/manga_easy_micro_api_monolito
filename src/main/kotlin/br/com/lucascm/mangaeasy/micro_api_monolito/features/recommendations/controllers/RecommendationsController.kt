package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.CreateRecommendationDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.services.RecommendationsService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/recommendations")
@Tag(name = "Recommendations")
class RecommendationsController {

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var recommendationsService: RecommendationsService

    @GetMapping("/v1")
    fun list(
        @RequestParam page: Int?
    ): List<RecommendationsEntity> {
        return recommendationsService.list(page ?: 0)
    }

    @GetMapping("/v1/{id}")
    fun getById(@PathVariable id: String): RecommendationsEntity {
        return recommendationsService.findById(id)
    }

    @DeleteMapping("/v1/{id}")
    fun delete(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable id: String,
    ) {
        handlerPermissionUser.handleIsAdmin(userAuth)
        recommendationsService.delete(id)
    }

    @PostMapping("/v1")
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreateRecommendationDto,
    ): RecommendationsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        body.validationValues()
        return recommendationsService.create(body)
    }

    @PutMapping("/v1/{id}")
    @ResponseBody
    fun update(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreateRecommendationDto,
        @PathVariable id: String,
    ): RecommendationsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        body.validationValues()
        return recommendationsService.update(body, id)
    }

    @PutMapping("/v1/{id}/images")
    fun uploadImage(
        @RequestPart file: MultipartFile,
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth,
    ): RecommendationsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        return recommendationsService.updateImage(id, file)
    }

    @GetMapping("/v1/{title}/anilist")
    fun getAnilistRecommendation(@PathVariable title: String): List<RecommendationsEntity> {
        if (title.isEmpty()) {
            throw BusinessException("Campo title não pode ser vazio")
        }
        return recommendationsService.anilist(title)
    }
}