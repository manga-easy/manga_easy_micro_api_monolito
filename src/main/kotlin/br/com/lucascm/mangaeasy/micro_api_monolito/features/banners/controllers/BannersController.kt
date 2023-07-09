package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetIsUserAdminService
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.entities.BannersEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.repositories.BannersRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.entities.PermissionsEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.Date

@RestController
@RequestMapping("/v1/banners")
class BannersController(@Autowired val repository: BannersRepository,
                        @Autowired val getIsUserAdmin: GetIsUserAdminService
) {
    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam status : String?,
             @RequestParam idhost : Int?
    ) : ResultEntity<BannersEntity> {
        try {
            val result: List<BannersEntity> = repository.findAll()
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return ResultEntity(
                total = 0,
                status = StatusResultEnum.ERROR,
                data = listOf(),
                message = e.message
            )
        }
    }

    @DeleteMapping("/{uid}")
    @ResponseBody
    fun delete(authentication: Authentication,
               @PathVariable uid : String
    ) : ResultEntity<BannersEntity> {
        try {
            val isUserAdmin = getIsUserAdmin.get(authentication.principal.toString())

            if (!isUserAdmin){
                throw BusinessException("O usuario não tem permissão")
            }
            val result = repository.findByUid(uid)

            if (result == null) throw BusinessException("Banner não encontrado")

            repository.delete(result)

            return ResultEntity(
                total = 0,
                status = StatusResultEnum.SUCCESS,
                data = emptyList(),
                message = "Deletado com sucesso"
            )
        } catch (e: Exception) {
            return HandleExceptions<BannersEntity>().handleCatch(e)
        }
    }

    @PostMapping()
    @ResponseBody
    fun create(authentication: Authentication,
               @RequestBody body : BannersEntity
    ) : ResultEntity<BannersEntity> {
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
            return HandleExceptions<BannersEntity>().handleCatch(e)
        }
    }
    @PutMapping("/{uid}")
    @ResponseBody
    fun update(authentication: Authentication,
               @RequestBody body : BannersEntity,
               @PathVariable uid: String
    ) : ResultEntity<BannersEntity> {
        try {
            handleValidatorWrite(authentication, body)
            val result = repository.findByUid(uid)
            if (result == null){
                throw BusinessException("Banner não encontrado")
            }
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
            return HandleExceptions<BannersEntity>().handleCatch(e)
        }
    }

    fun handleValidatorWrite(authentication: Authentication, body: BannersEntity){
        val isUserAdmin = getIsUserAdmin.get(authentication.principal.toString())

        if (!isUserAdmin){
            throw BusinessException("O usuario não tem permissão")
        }
        if (body.image.isEmpty()) {
            throw BusinessException("Campo Image não pode ser vazio")
        }
        if (body.link.isEmpty()) {
            throw BusinessException("Campo link não pode ser vazio")
        }
    }
}