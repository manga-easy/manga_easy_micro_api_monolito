package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerUserAdmin
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.RecommendationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/recommendations")
class RecommendationsController(@Autowired val repository: RecommendationsRepository,
                                @Autowired val getIsUserAdmin: HandlerUserAdmin
) {
    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam status: String?,
             @RequestParam idhost: Int?,
             @RequestParam isAll: Boolean?
    ) : ResultEntity {
        try {
            val result: List<RecommendationsEntity> = if (isAll != false){
                repository.findAllByOrderByUpdatedatDesc()
            }else{
                repository.findTop5ByOrderByUpdatedatDesc()
            }
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return HandleExceptions().handleCatch(e)
        }
    }

    @DeleteMapping("/{uid}")
    @ResponseBody
    fun delete(authentication: Authentication,
               @PathVariable uid : String
    ) : ResultEntity {
        try {
            val isUserAdmin = getIsUserAdmin.get(authentication.principal.toString())

            if (!isUserAdmin){
                throw BusinessException("O usuario não tem permissão")
            }
            val result = repository.findByUid(uid)

            if (result == null) throw BusinessException("Recomendação não encontrado")

            repository.delete(result)

            return ResultEntity(
                total = 0,
                status = StatusResultEnum.SUCCESS,
                data = emptyList(),
                message = "Deletado com sucesso"
            )
        } catch (e: Exception) {
            return HandleExceptions().handleCatch(e)
        }
    }

    @PostMapping()
    @ResponseBody
    fun create(authentication: Authentication,
               @RequestBody body : RecommendationsEntity
    ) : ResultEntity {
        try {
            handleValidatorWrite(authentication, body)
            val resultVCheck = repository.findByUniqueid(body.uniqueid)
            if (resultVCheck != null){
                throw BusinessException("Esse mangá já tem recomendação")
            }
            val result = repository.save(body.apply {
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
            return HandleExceptions().handleCatch(e)
        }
    }
    @PutMapping("/{uid}")
    @ResponseBody
    fun update(authentication: Authentication,
               @RequestBody body : RecommendationsEntity,
               @PathVariable uid: String
    ) : ResultEntity {
        try {
            handleValidatorWrite(authentication, body)
            val result = repository.findByUid(uid)
            if (result == null){
                throw BusinessException("Banner não encontrado")
            }
            val resultUpdate = result.apply {
                updatedat = Date().time
                link = body.link
                artistid = body.artistid
                artistname = body.artistname
                title = body.title
                uniqueid = body.uniqueid
            }
            repository.save(resultUpdate)
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Alterado com sucesso"
            )
        } catch (e: Exception) {
            return HandleExceptions().handleCatch(e)
        }
    }

    fun handleValidatorWrite(authentication: Authentication, body: RecommendationsEntity){
        val isUserAdmin = getIsUserAdmin.get(authentication.principal.toString())

        if (!isUserAdmin){
            throw BusinessException("O usuario não tem permissão")
        }
        if (body.uniqueid.isEmpty()) {
            throw BusinessException("Campo uniqueid não pode ser vazio")
        }
        if (body.title.isEmpty()) {
            throw BusinessException("Campo title não pode ser vazio")
        }
        if (body.link.isEmpty()) {
            throw BusinessException("Campo link não pode ser vazio")
        }
        if ((body.artistid ?: "").isEmpty()) {
            throw BusinessException("Campo artistid não pode ser vazio")
        }
        if ((body.artistname ?: "").isEmpty()) {
            throw BusinessException("Campo artistname não pode ser vazio")
        }
    }
}