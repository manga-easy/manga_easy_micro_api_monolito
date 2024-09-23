package br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.entities.CreatePermissionDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.entities.PermissionsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.repositories.PermissionsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/permissions")
@Tag(name = "Permissions")
class PermissionsController {
    @Autowired
    lateinit var repository: PermissionsRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
    fun list(@AuthenticationPrincipal userAuth: UserAuth): List<PermissionsEntity> {
        handlerPermissionUser.handleIsAdmin(userAuth)
        return repository.findAll()
    }

    @GetMapping("/v1/users/{userId}")
    fun getByUserId(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable userId: String
    ): PermissionsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        return repository.findByUserId(userId)
            ?: throw BusinessException("Permissão Não encontrado")
    }

    @GetMapping("/v1/{id}")
    fun getById(@PathVariable id: String): PermissionsEntity {
        return repository.findById(id).getOrNull()
            ?: throw BusinessException("Permission não encontrado")
    }

    @PostMapping("/v1")
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreatePermissionDto
    ): PermissionsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val permission = repository.findByUserId(body.userId)
        if (permission != null) {
            throw BusinessException("O usuario ja tem um nivel de permissão")
        }
        if (body.value >= 90) {
            throw BusinessException("Permissão é maior que o permitido")
        }
        return repository.save(
            PermissionsEntity(
                createdAt = Date().time,
                updatedAt = Date().time,
                userId = body.userId,
                level = body.value
            )
        )
    }

    @PutMapping("/v1")
    fun update(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreatePermissionDto
    ): PermissionsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val permission = repository.findByUserId(body.userId)
            ?: throw BusinessException("O registro não encontrado")

        val permissionUpdated = permission.copy(
            updatedAt = Date().time,
            level = body.value
        )
        return repository.save(permissionUpdated)
    }

    @DeleteMapping("/v1/{id}")
    fun delete(@AuthenticationPrincipal userAuth: UserAuth, @PathVariable id: String) {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val permission = repository.findById(id)
        if (!permission.isPresent) {
            throw BusinessException("O registro não encontrado")
        }
        repository.deleteById(id)
    }

}