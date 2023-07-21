package br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerUserAdmin
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.entities.PermissionsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.repositories.PermissionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/permissions")
class PermissionsController(@Autowired val repository: PermissionsRepository,
                            @Autowired val getIsUserAdmin: HandlerUserAdmin) {
    @GetMapping("/list")
    @ResponseBody
    fun list(authentication: Authentication) : ResultEntity<PermissionsEntity> {
        try {
            val isUserAdmin = getIsUserAdmin.get(authentication.principal.toString())
            if (!isUserAdmin){
                throw BusinessException("O usuario não tem permissão")
            }
            val result: List<PermissionsEntity> = repository.findAll()
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return HandleExceptions<PermissionsEntity>().handleCatch(e)
        }
    }
    @PostMapping
    @ResponseBody
    fun create(authentication: Authentication, @RequestBody body: PermissionsEntity) : ResultEntity<PermissionsEntity> {
        try {
            val isUserAdmin = getIsUserAdmin.get(authentication.principal.toString())
            if (!isUserAdmin){
                throw BusinessException("O usuario não tem permissão")
            }
            if (body.userid == null){
                throw BusinessException("O userid não pode ser nulo")
            }

            val permission = repository.findByUserid(body.userid!!)
            if (permission != null){
               throw BusinessException("O usuario ja tem um nivel de permissão")
            }
            if (body.value == null){
                throw BusinessException("O value não pode ser nulo")
            }
            if (body.value!! >= 90){
                throw BusinessException("Permissão é maior que o permitido")
            }

            body.uid = GetUidByFeature().get("permissions")
            body.createdat = Date().time
            body.updatedat = Date().time
            val result = repository.save(body)
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Criado com sucesso"
            )
        } catch (e: Exception) {
            return HandleExceptions<PermissionsEntity>().handleCatch(e)
        }
    }

    @PutMapping
    @ResponseBody
    fun update(authentication: Authentication, @RequestBody body: PermissionsEntity) : ResultEntity<PermissionsEntity> {
        try {
            val isUserAdmin = getIsUserAdmin.get(authentication.principal.toString())

            if (!isUserAdmin){
                throw BusinessException("O usuario não tem permissão")
            }

            if (body.value == null){
                throw BusinessException("O value não pode ser nulo")
            }
            val permission = repository.findByUserid(body.userid!!) ?: throw BusinessException("O registro não encontrado")

            val permissionUpdated = permission.apply {
                updatedat = Date().time
                value = body.value
            }
            repository.save(permissionUpdated)
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(permissionUpdated),
                message = "Update com sucesso"
            )
        } catch (e: Exception) {
            return HandleExceptions<PermissionsEntity>().handleCatch(e)
        }
    }

    @DeleteMapping("/{uid}")
    @ResponseBody
    fun delete(authentication: Authentication, @PathVariable uid: String) : ResultEntity<PermissionsEntity> {
        try {
            val isUserAdmin = getIsUserAdmin.get(authentication.principal.toString())

            if (!isUserAdmin){
                throw BusinessException("O usuario não tem permissão")
            }
            val permission = repository.findByUid(uid) ?: throw BusinessException("O registro não encontrado")

            repository.delete(permission)
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(permission),
                message = "Deletado com sucesso"
            )
        } catch (e: Exception) {
            return HandleExceptions<PermissionsEntity>().handleCatch(e)
        }
    }

}