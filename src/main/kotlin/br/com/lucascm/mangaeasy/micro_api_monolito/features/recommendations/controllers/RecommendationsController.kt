package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.controllers

import CacheAnilistEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.BucketRecommendationsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.RecommendationAnilistCache
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.RecommendationAnilistRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.RecommendationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*


@RestController
@RequestMapping("/v1/recommendations")
class RecommendationsController {
    @Autowired
    lateinit var recommendationsRepository: RecommendationsRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var bucketRecommendationsRepository: BucketRecommendationsRepository

    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @Autowired
    lateinit var recommendationAnilistCache: RecommendationAnilistCache

    @Autowired
    lateinit var recommendationAnilistRepository: RecommendationAnilistRepository

    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @GetMapping("/list")
    @ResponseBody
    fun list(
        @RequestParam status: String?,
        @RequestParam idhost: Int?,
        @RequestParam isAll: Boolean?,
    ): ResultEntity {
        return try {
            val result: List<RecommendationsEntity> = if (isAll != false) {
                recommendationsRepository.findAllByOrderByUpdatedatDesc()
            } else {
                recommendationsRepository.findTop5ByOrderByUpdatedatDesc()
            }
            ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @DeleteMapping("/{uid}")
    @ResponseBody
    fun delete(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable uid: String,
    ): ResultEntity {
        return try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            val result =
                recommendationsRepository.findByUid(uid) ?: throw BusinessException("Recomendação não encontrado")
            bucketRecommendationsRepository.deleteImage(result.uniqueid)
            recommendationsRepository.delete(result)
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
        @RequestBody body: RecommendationsEntity,
    ): ResultEntity {
        try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            handleValidatorWrite(body)
            val resultVCheck = recommendationsRepository.findByUniqueid(body.uniqueid)
            if (resultVCheck != null) {
                throw BusinessException("Esse mangá já tem recomendação")
            }
            val result = recommendationsRepository.save(body.apply {
                createdat = Date().time
                updatedat = Date().time
                datacria = Date().time
                uid = GetUidByFeature().get("recommendations")
            })
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
        @RequestBody body: RecommendationsEntity,
        @PathVariable uid: String,
    ): ResultEntity {
        return try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            handleValidatorWrite(body)
            val result = recommendationsRepository.findByUid(uid) ?: throw BusinessException("Banner não encontrado")
            val resultUpdate = result.apply {
                updatedat = Date().time
                link = body.link
                artistid = body.artistid
                artistname = body.artistname
                title = body.title
                uniqueid = body.uniqueid
            }
            recommendationsRepository.save(resultUpdate)
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
                bucketRecommendationsRepository.saveImage(uniqueid, file, file.contentType!!)
                imageResult = bucketRecommendationsRepository.getLinkImage(uniqueid)
            }
            val result = recommendationsRepository.save(
                find.copy(
                    link = imageResult!!,
                    updatedat = Date().time
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
        @PathVariable title: String, @AuthenticationPrincipal userAuth: UserAuth,
    ): ResultEntity {
        return try {
            val cacheResult = recommendationAnilistCache.findById(convertUniqueid(title))
            if (cacheResult.isPresent) {
                return ResultEntity(cacheResult.get().recommendation)
            }
            val recommendationAnilist = recommendationAnilistRepository.getRecommendationByTitle(title)
            val recommendations = mutableListOf<RecommendationsEntity>()
            for (e in recommendationAnilist) {
                val catalog = findCatalog(
                    e.title.romaji ?: "",
                    e.title.english ?: ""
                )
                    ?: continue

                val recommendation = recommendationsRepository.findByTitle(catalog.name)
                if (recommendation != null) {
                    recommendations.add(recommendation)
                    continue
                }

                if (e.bannerImage != null) {
                    recommendations.add(
                        RecommendationsEntity(
                            title = catalog.name,
                            link = e.bannerImage,
                            uniqueid = catalog.uniqueid
                        )
                    )
                }
            }
            recommendationAnilistCache.save(
                CacheAnilistEntity(
                    id = convertUniqueid(title),
                    title = title,
                    recommendation = recommendations,
                )
            )
            ResultEntity(recommendations)
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    fun handleValidatorWrite(body: RecommendationsEntity) {
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