package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.CreateRecommendationDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.BucketRecommendationsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.RecommendationsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.services.RecommendationsService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*


@RestController
@RequestMapping("/recommendations")
@Tag(name = "Recommendations")
class RecommendationsController {
    @Autowired
    lateinit var recommendationsRepository: RecommendationsRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var bucketRecommendationsRepository: BucketRecommendationsRepository


    @Autowired
    lateinit var recommendationsService: RecommendationsService

    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var profileRepository: ProfileRepository

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
        val result = recommendationsRepository.findById(id).get()
        recommendationsRepository.deleteById(id)
        bucketRecommendationsRepository.deleteImage(result.uniqueid)
    }

    @PostMapping("/v1")
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreateRecommendationDto,
    ): RecommendationsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        body.validationValues()
        val resultVCheck = recommendationsRepository.findByUniqueid(body.uniqueId)
        if (resultVCheck != null) {
            throw BusinessException("Mangá já tem recomendação")
        }
        val user = profileRepository.findByUserId(body.artistId)
            ?: throw BusinessException("Artista não tem perfil")
        return recommendationsRepository.save(
            RecommendationsEntity(
                createdAt = Date().time,
                title = body.title,
                updatedAt = Date().time,
                uniqueid = body.uniqueId,
                artistId = user.name
            )
        )
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
        val result = recommendationsRepository.findById(id)
        if (!result.isPresent) throw BusinessException("Recomendação não encontrado")
        val user = profileRepository.findByUserId(body.artistId)
            ?: throw BusinessException("Artista não tem perfil")
        return recommendationsRepository.save(
            RecommendationsEntity(
                title = body.title,
                updatedAt = Date().time,
                uniqueid = body.uniqueId,
                artistId = body.uniqueId,
                artistName = user.name,
            )
        )
    }

    @PutMapping("/v1/{id}/images")
    fun uploadImage(
        @RequestPart file: MultipartFile,
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth,
    ): RecommendationsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val imageResult: String?
        val find = recommendationsRepository.findById(id)
        if (!find.isPresent) throw BusinessException("Recomendação não encontrada")
        val entity = find.get()
        bucketRecommendationsRepository.saveImage(entity.uniqueid, file)
        imageResult = bucketRecommendationsRepository.getLinkImage(entity.uniqueid)
        return recommendationsRepository.save(
            entity.copy(
                link = imageResult,
                updatedAt = Date().time
            )
        )
    }

    @GetMapping("/v1/{title}/anilist")
    fun getAnilistRecommendation(@PathVariable title: String): List<RecommendationsEntity> {
        if (title.isEmpty()) {
            throw BusinessException("Campo title não pode ser vazio")
        }
        return recommendationsService.anilist(title)
    }
}