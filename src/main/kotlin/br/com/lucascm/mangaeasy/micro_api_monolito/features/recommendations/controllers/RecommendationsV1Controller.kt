package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsV1Dto
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
@RequestMapping("/v1/recommendations")
@Deprecated("Remover 0.18 -> 0.20")
@Tag(name = "Recommendations")
class RecommendationsV1Controller {
    @Autowired
    lateinit var recommendationsRepository: RecommendationsRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var bucketRecommendationsRepository: BucketRecommendationsRepository

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

    @DeleteMapping("/{id}")
    @ResponseBody
    fun delete(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable id: String,
    ): ResultEntity {
        return try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            val result =
                recommendationsRepository.findById(id)
            if (!result.isPresent) throw BusinessException("Recomendação não encontrado")
            recommendationsRepository.deleteById(id)
            bucketRecommendationsRepository.deleteImage(result.get().uniqueid)
            ResultEntity(
                total = 0,
                status = StatusResultEnum.SUCCESS,
                data = emptyList(),
                message = "Deletado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PostMapping
    @ResponseBody
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: RecommendationsV1Dto,
    ): ResultEntity {
        try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            handleValidatorWrite(body)
            val resultVCheck = recommendationsRepository.findByUniqueid(body.uniqueid)
            if (resultVCheck != null) {
                throw BusinessException("Esse mangá já tem recomendação")
            }
            val result = recommendationsRepository.save(
                RecommendationsEntity(
                    createdAt = Date().time,
                    updatedAt = Date().time,
                    artistId = body.artistid,
                    uniqueid = body.uniqueid,
                    title = body.title
                )
            )
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Criado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/{uid}")
    @ResponseBody
    fun update(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: RecommendationsV1Dto,
        @PathVariable uid: String,
    ): ResultEntity {
        return try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            handleValidatorWrite(body)
            val find = recommendationsRepository.findById(uid)
            if (!find.isPresent) {
                throw BusinessException("Banner não encontrado")
            }
            val result = recommendationsRepository.save(
                RecommendationsEntity(
                    updatedAt = Date().time,
                    artistId = body.artistid,
                    uniqueid = body.uniqueid,
                    title = body.title
                )
            )
            ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Alterado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/{uniqueid}/image")
    fun uploadImage(
        @RequestPart file: MultipartFile?,
        @PathVariable uniqueid: String,
        @AuthenticationPrincipal userAuth: UserAuth,
    ): ResultEntity {
        return try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            var imageResult: String? = null
            val find: RecommendationsEntity = recommendationsRepository.findByUniqueid(uniqueid)
                ?: throw BusinessException("Recomendação não encontrada")
            if (file != null) {
                bucketRecommendationsRepository.saveImage(uniqueid, file)
                imageResult = bucketRecommendationsRepository.getLinkImage(uniqueid)
            }
            val result = recommendationsRepository.save(
                find.copy(
                    link = imageResult!!,
                    updatedAt = Date().time
                )
            )
            ResultEntity(
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                total = 1,
                message = "Sucesso"
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

    fun handleValidatorWrite(body: RecommendationsV1Dto) {
        if (body.uniqueid.isEmpty()) {
            throw BusinessException("Campo uniqueid não pode ser vazio")
        }
        if (body.title.isEmpty()) {
            throw BusinessException("Campo title não pode ser vazio")
        }
        if ((body.artistid ?: "").isEmpty()) {
            throw BusinessException("Campo artistid não pode ser vazio")
        }
        if ((body.artistname ?: "").isEmpty()) {
            throw BusinessException("Campo artistname não pode ser vazio")
        }
    }

    private fun convertUniqueid(titleManga: String): String {
        // Lista de termos a serem removidos do título do manga
        val termos = listOf(
            "(br)",
            "(color)",
            "pt-br"
        )
        // Converter o título do manga para letras minúsculas
        var titleMangaLower = titleManga.toLowerCase()

        // Remover termos específicos do título do manga
        for (termo in termos) {
            titleMangaLower = titleMangaLower.replace(termo, "")
        }

        // Remover caracteres especiais do título do manga
        return titleMangaLower.replace(Regex("[^A-Za-z0-9]"), "")
    }

    private fun findCatalog(romanji: String, english: String): CatalogEntity? {
        if (romanji.isEmpty() && english.isEmpty()) return null
        val catalog = catalogRepository.findByUniqueid(convertUniqueid(romanji))
        if (catalog != null) return catalog
        return catalogRepository.findByUniqueid(convertUniqueid(english))
    }

}