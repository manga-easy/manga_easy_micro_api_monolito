package br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetIsUserAdminService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.entities.PermissionsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.repositories.PermissionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

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

}