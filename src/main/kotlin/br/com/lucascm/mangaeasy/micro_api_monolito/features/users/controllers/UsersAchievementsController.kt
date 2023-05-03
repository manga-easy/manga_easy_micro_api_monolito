package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersAchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
class UsersAchievementsController(@Autowired val repository: UsersAchievementsRepository) {
    @GetMapping("/{uid}/achievements")
    @ResponseBody
    fun listAchievements(@PathVariable uid: String) : ResultEntity<UsersAchievementsEntity> {
        try {
            val result: List<UsersAchievementsEntity> = repository.findAllByUserid(uid)
            return ResultEntity<UsersAchievementsEntity>(
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