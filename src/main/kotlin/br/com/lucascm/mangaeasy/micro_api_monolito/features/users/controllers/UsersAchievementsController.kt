package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.AchievementsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.CreateUserAchievementDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersAchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/users/{userId}")
@Tag(name = "Users")
class UsersAchievementsController {
    @Autowired
    lateinit var repository: UsersAchievementsRepository

    @Autowired
    lateinit var achievementsRepository: AchievementsRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser


    @GetMapping("/v1/achievements")
    fun listAchievements(
        @PathVariable userId: String,
        @RequestParam achievementId: String?,
        @AuthenticationPrincipal userAuth: UserAuth
    ): List<UsersAchievementsEntity> {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId);
        return if (achievementId == null) {
            repository.findAllByUserId(userId)
        } else {
            repository.findAllByUserIdAndAchievementId(userId, achievementId)
        }
    }

    @DeleteMapping("/v1/achievements/{achievementId}")
    fun removeAchievement(
        @PathVariable userId: String,
        @PathVariable achievementId: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ) {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val resultAchievements = repository.findAllByUserIdAndAchievementId(
            userId = userId,
            achievementId = achievementId,
        )
        if (resultAchievements.isEmpty()) {
            throw BusinessException("Emblema não encontrado")
        }
        repository.delete(
            resultAchievements.first()
        )
    }

    @PostMapping("/v1/achievements")
    fun addUserAchievement(
        @PathVariable userId: String,
        @RequestBody body: CreateUserAchievementDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): UsersAchievementsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val resultAchievements = achievementsRepository.findById(body.achievementId).getOrNull()
            ?: throw BusinessException("Emblema não encontrado")
        val resultUsers = repository.findAllByUserIdAndAchievementId(
            userId,
            resultAchievements.id!!
        )
        if (resultUsers.isNotEmpty()) {
            throw BusinessException("Emblema já adquirido")
        }
        return repository.save(
            UsersAchievementsEntity(
                createdAt = Date().time,
                userId = userId,
                achievementId = body.achievementId
            )
        )
    }
}