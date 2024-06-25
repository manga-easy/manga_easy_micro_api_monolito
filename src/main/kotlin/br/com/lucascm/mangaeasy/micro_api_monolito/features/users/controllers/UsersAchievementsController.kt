package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.AchievementsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersAchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/v1/users")
class UsersAchievementsController {
    @Autowired
    lateinit var repository: UsersAchievementsRepository

    @Autowired
    lateinit var achievementsRepository: AchievementsRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @GetMapping("/{uid}/achievements")
    fun listAchievements(
        @PathVariable uid: String,
        @RequestParam idEmblema: String?,
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        try {
            handlerPermissionUser.handleIsOwnerToken(userAuth, uid);
            val result: List<UsersAchievementsEntity> = if (idEmblema == null) {
                repository.findAllByUserid(uid)
            } else {
                repository.findAllByUseridAndIdemblema(uid, idEmblema)
            }
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @DeleteMapping("/{uid}/achievements/{idAchieviment}")
    fun removeAchievement(
        @PathVariable uid: String,
        @PathVariable idAchieviment: String,
        @AuthenticationPrincipal userAuth: UserAuth
    )
            : ResultEntity {
        try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            val resultAchievements = repository.findAllByUseridAndIdemblema(
                userId = uid,
                idemblema = idAchieviment,
            )
            if (resultAchievements.isEmpty()) {
                throw BusinessException("Emblema não encontrado")
            }
            repository.delete(
                resultAchievements.first()
            )

            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(),
                message = "Removido com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @PostMapping("/{uid}/achievements")
    fun addUserAchievement(
        @PathVariable uid: String,
        @RequestBody body: UsersAchievementsEntity,
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            val resultAchievements = achievementsRepository.findById(body.idemblema).getOrNull()
                ?: throw BusinessException("Emblema não encontrado")
            val resultUsers = repository.findAllByUseridAndIdemblema(
                uid,
                resultAchievements.id!!
            )

            if (resultUsers.isNotEmpty()) {
                throw BusinessException("Emblema já adquirido")
            }
            val result = repository.save(
                body.copy(
                    uid = GetUidByFeature().get("users-achievements"),
                    createdat = Date().time,
                    timecria = Date().time,
                    updatedat = Date().time,
                    userid = uid,
                )
            )
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Adicionado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @PostMapping("/{uid}/achievements/acquire")
    fun acquire(
        @PathVariable uid: String,
        @RequestBody body: UsersAchievementsEntity,
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        try {
            handlerPermissionUser.handleIsOwnerToken(userAuth, uid);
            val resultEmblema = achievementsRepository.findById(body.idemblema).getOrNull()
                ?: throw BusinessException("Emblema não encontrado")
            if (resultEmblema.category != "evento") {
                throw BusinessException("Este tipo de emblema não pode ser resgatado")
            }
            if (!resultEmblema.reclaim) {
                throw BusinessException("Emblema não disponível")
            }
            val result: List<UsersAchievementsEntity> = repository.findAllByUseridAndIdemblema(uid, resultEmblema.id!!)

            if (result.isNotEmpty()) {
                throw BusinessException("Emblema já adquirido")
            }

            val resultSave = repository.save(
                body.copy(
                    idemblema = resultEmblema.id,
                    timecria = Date().time,
                    userid = uid,
                    createdat = Date().time,
                    updatedat = Date().time,
                    uid = GetUidByFeature().get("achievements"),
                )
            )

            return ResultEntity(
                total = 0,
                status = StatusResultEnum.SUCCESS,
                data = listOf(resultSave),
                message = "Emblema adquirido com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }
}