package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.VerifyUserIdPermissionService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersLevelsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersLevelsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
class UsersLevelsController(@Autowired val repository: UsersLevelsRepository,
                            @Autowired val verifyUserIdPermissionService: VerifyUserIdPermissionService) {
    @GetMapping("/{uid}/levels")
    @ResponseBody
    fun list(@PathVariable uid: String,
             @RequestParam idSeason: String,
             authentication: Authentication) : ResultEntity<UsersLevelsEntity> {
        try {
            verifyUserIdPermissionService.get(authentication, uid);
            val result = repository.findByTemporadaAndUserid(idSeason, uid)
            return ResultEntity(
                total = 1,
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