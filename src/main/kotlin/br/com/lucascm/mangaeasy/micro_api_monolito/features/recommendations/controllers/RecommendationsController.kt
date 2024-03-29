package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerUserAdmin
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.controllers.TYPE_CONTENT_IMAGE
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.BucketRecommendationsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.RecommendationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

const val LIMIT_FILE_SIZE_RECOMMENDATION = 2000000

@RestController
@RequestMapping("/v1/recommendations")
class RecommendationsController {
    @Autowired
    lateinit var recommendationsRepository: RecommendationsRepository

    @Autowired
    lateinit var handlerUserAdmin: HandlerUserAdmin

    @Autowired
    lateinit var bucketRecommendationsRepository: BucketRecommendationsRepository

    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @GetMapping("/list")
    @ResponseBody
    fun list(
        @RequestParam status: String?,
        @RequestParam idhost: Int?,
        @RequestParam isAll: Boolean?
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
        authentication: Authentication,
        @PathVariable uid: String
    ): ResultEntity {
        return try {
            handlerUserAdmin.handleIsAdmin(authentication.principal.toString())
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
        authentication: Authentication,
        @RequestBody body: RecommendationsEntity
    ): ResultEntity {
        try {
            handlerUserAdmin.handleIsAdmin(authentication.principal.toString())
            handleValidatorWrite(authentication, body)
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
        authentication: Authentication,
        @RequestBody body: RecommendationsEntity,
        @PathVariable uid: String
    ): ResultEntity {
        return try {
            handlerUserAdmin.handleIsAdmin(authentication.principal.toString())
            handleValidatorWrite(authentication, body)
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
        authentication: Authentication
    ): ResultEntity {
        return try {
            handlerUserAdmin.handleIsAdmin(authentication.principal.toString())
            var imageResult: String? = null
            val find: RecommendationsEntity = recommendationsRepository.findByUniqueid(uniqueid)
                ?: throw BusinessException("Recomendação não encontrada")
            if (file != null) {
                validateImage(file)
                bucketRecommendationsRepository.saveImage(uniqueid, file, file.contentType!!)
                imageResult = bucketRecommendationsRepository.getLinkImage(uniqueid)
            }
            val result = recommendationsRepository.save(find.copy(
                link = imageResult!!,
                updatedat = Date().time
            ))
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

    fun handleValidatorWrite(authentication: Authentication, body: RecommendationsEntity) {
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

    private fun validateImage(file: MultipartFile) {
        val limit = LIMIT_FILE_SIZE_RECOMMENDATION
        if (file.size > limit) throw BusinessException("Imagem maior que o permitido: ${limit.toString()[0]}mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) throw BusinessException("Tipo de arquivo não permitido.")
    }
}