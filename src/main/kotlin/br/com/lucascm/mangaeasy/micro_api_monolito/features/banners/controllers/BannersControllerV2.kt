package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerUserAdmin
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.entities.BannersEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.repositories.BannersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v2/banners")
class BannersControllerV2 {
    @Autowired
    lateinit var repository: BannersRepository

    @Autowired
    lateinit var getIsUserAdmin: HandlerUserAdmin

    @GetMapping("/list")
    fun list(
        @RequestParam status: String?,
        @RequestParam idhost: Int?
    ): ResultEntity {
        return try {
            val result: List<BannersEntity> = repository.findAll()
            ResultEntity(result)
        } catch (e: Exception) {
            HandleExceptions().handleCatch(e)
        }
    }

    @DeleteMapping("/{uid}")
    fun delete(
        authentication: Authentication,
        @PathVariable uid: String
    ): ResultEntity {
        try {
            getIsUserAdmin.handleIsAdmin(authentication.principal.toString())

            val result = repository.findByUid(uid)
                ?: throw BusinessException("Banner não encontrado")

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

    @PostMapping
    fun create(
        authentication: Authentication,
        @RequestBody body: BannersEntity
    ): ResultEntity {
        try {
            handleValidatorWrite(authentication, body)
            val result = repository.save(body.apply {
                createdat = Date().time
                updatedat = Date().time
                uid = GetUidByFeature().get("banners")
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
    fun update(
        authentication: Authentication,
        @RequestBody body: BannersEntity,
        @PathVariable uid: String
    ): ResultEntity {
        try {
            handleValidatorWrite(authentication, body)
            val result = repository.findByUid(uid)
                ?: throw BusinessException("Banner não encontrado")
            val resultUpdate = result.apply {
                updatedat = Date().time
                link = body.link
                image = body.image
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

    fun handleValidatorWrite(authentication: Authentication, body: BannersEntity) {
        getIsUserAdmin.handleIsAdmin(authentication.principal.toString())

        if (body.image.isEmpty()) {
            throw BusinessException("Campo Image não pode ser vazio")
        }
        if (body.link.isEmpty()) {
            throw BusinessException("Campo link não pode ser vazio")
        }
    }
}