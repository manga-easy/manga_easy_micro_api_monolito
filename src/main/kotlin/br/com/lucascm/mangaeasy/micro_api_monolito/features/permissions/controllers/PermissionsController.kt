package br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetIsUserAdminService
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.entities.PermissionsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.repositories.PermissionsRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/permissions")
class PermissionsController(@Autowired val repository: PermissionsRepository,
                            @Autowired val getIsUserAdmin: GetIsUserAdminService) {
    @GetMapping("/list")
    @ResponseBody
    fun list(authentication: Authentication) : ResultEntity<PermissionsEntity> {
        try {
            val isUserAdmin = getIsUserAdmin.get(authentication.principal.toString())
            if (!isUserAdmin){
                throw Exception("O usuario não tem permissão")
            }
            val result: List<PermissionsEntity> = repository.findAll()
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
    @PostMapping
    @ResponseBody
    fun create(authentication: Authentication, @RequestBody body: PermissionsEntity) : ResultEntity<PermissionsEntity> {
        try {
            val isUserAdmin = getIsUserAdmin.get(authentication.principal.toString())
            if (!isUserAdmin){
                throw Exception("O usuario não tem permissão")
            }
            if (body.userid == null){
                throw Exception("O userid não pode ser nulo")
            }
            val permission = repository.findByUserid(body.userid!!)
            if (permission != null){
               throw Exception("O usuario ja tem um nivel de permissão")
            }
            if (body.value == null){
                throw Exception("O value não pode ser nulo")
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
            KotlinLogging.logger("permissions").catching(e)
            return ResultEntity(
                total = 0,
                status = StatusResultEnum.ERROR,
                data = listOf(),
                message = e.message
            )
        }
    }

    @PutMapping
    @ResponseBody
    fun update(authentication: Authentication, @RequestBody body: PermissionsEntity) : ResultEntity<PermissionsEntity> {
        try {
            val isUserAdmin = getIsUserAdmin.get(authentication.principal.toString())

            if (!isUserAdmin){
                throw Exception("O usuario não tem permissão")
            }
            val permission = repository.findByUserid(body.userid!!) ?: throw Exception("O registro não encontrado")

            permission.apply {
                updatedat = Date().time
                value = body.value
            }
            repository.save(permission)
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(permission),
                message = "Update com sucesso"
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

}