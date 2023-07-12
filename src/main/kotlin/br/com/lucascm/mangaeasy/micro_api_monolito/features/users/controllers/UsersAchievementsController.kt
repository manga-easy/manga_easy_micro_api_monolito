package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetIsUserAdminService
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.VerifyUserIdPermissionService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.AchievementsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersAchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.Date

@RestController
@RequestMapping("/v1/users")
class UsersAchievementsController() {
    @Autowired lateinit var repository: UsersAchievementsRepository
    @Autowired lateinit var achievementsRepository: AchievementsRepository
    @Autowired lateinit var verifyUserIdPermissionService: VerifyUserIdPermissionService
    @Autowired lateinit var getIsUserAdminService: GetIsUserAdminService
    @Autowired lateinit var handleExceptions: HandleExceptions<UsersAchievementsEntity>
    @GetMapping("/{uid}/achievements")
    @ResponseBody
    fun listAchievements(@PathVariable uid: String,
                         @RequestParam idEmblema: String?,
                         authentication: Authentication) : ResultEntity<UsersAchievementsEntity> {
        try {
            verifyUserIdPermissionService.get(authentication, uid);
            val result: List<UsersAchievementsEntity> = if (idEmblema == null){
                repository.findAllByUserid(uid)
            }else{
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

    @PostMapping("/{uid}/achievements")
    @ResponseBody
    fun addUserAchievement(@PathVariable uid: String,
                           @RequestBody body: UsersAchievementsEntity,
                           authentication: Authentication
    ) : ResultEntity<UsersAchievementsEntity> {
        try {
            val isUserAdmin = getIsUserAdminService.get(
                authentication.principal.toString()
            )
            if (!isUserAdmin){
                throw BusinessException("O usuario não tem permissão")
            }
            val resultAchievements = achievementsRepository.findAllByUid(body.idemblema)
            if (resultAchievements.isEmpty()){
                throw BusinessException("Emblema não encontrado")
            }
            val resultUsers = repository.findAllByUseridAndIdemblema(
                uid,
                resultAchievements.first().uid!!
            )

            if (resultUsers.isNotEmpty()){
                throw BusinessException("Emblema já adquirido")
            }
            val result = repository.save(body.copy(
                uid = GetUidByFeature().get("users-achievements"),
                createdat = Date().time,
                timecria = Date().time,
                updatedat = Date().time,
                userid = uid,
            ))
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
    @ResponseBody
    fun acquire(@PathVariable uid: String,
                @RequestBody body: UsersAchievementsEntity,
                authentication: Authentication) : ResultEntity<UsersAchievementsEntity> {
        try {
            verifyUserIdPermissionService.get(authentication, uid);
            val resultEmblema =  achievementsRepository.findAllByUid(body.idemblema)
            if (resultEmblema.isEmpty()){
                throw BusinessException("Emblema não encontrado")
            }
            val emblema = resultEmblema.first()
            if (emblema.categoria != "evento"){
                throw BusinessException("Este tipo de emblema não pode ser resgatado")
            }
            if (!emblema.disponivel!!){
                throw BusinessException("Emblema não disponível")
            }
            val result: List<UsersAchievementsEntity> = repository.findAllByUseridAndIdemblema(uid, emblema.uid!!)

            if (result.isNotEmpty()){
                throw BusinessException("Emblema já adquirido")
            }

            val resultSave = repository.save(body.copy(
                idemblema = emblema.uid!!,
                timecria = Date().time,
                userid = uid,
                createdat = Date().time,
                updatedat = Date().time,
                uid = GetUidByFeature().get("achievements"),
            ))

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