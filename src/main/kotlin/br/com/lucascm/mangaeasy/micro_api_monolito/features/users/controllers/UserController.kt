package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UserEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UserRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
@Tag(name = "Users")
class UserController {
    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
    fun getUsers(
        @RequestParam search: String?,
        @AuthenticationPrincipal userAuth: UserAuth
    ): List<UserEntity> {
        handlerPermissionUser.handleIsAdmin(userAuth)
        return repository.search(search)

    }

    @GetMapping("/v1/{id}")
    fun get(
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ): UserEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        return repository.getId(id)
    }

}