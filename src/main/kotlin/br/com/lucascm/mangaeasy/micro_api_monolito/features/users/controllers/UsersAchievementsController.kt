package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
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
class UsersAchievementsController(@Autowired val repository: UsersAchievementsRepository,
                                  @Autowired val achievementsRepository: AchievementsRepository,
                                  @Autowired val verifyUserIdPermissionService: VerifyUserIdPermissionService) {
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
            return ResultEntity(
                total = 0,
                status = StatusResultEnum.ERROR,
                data = listOf(),
                message = e.message
            )
        }
    }

    @PostMapping("/{uid}/achievements/acquire")
    @ResponseBody
    fun acquire(@PathVariable uid: String,
                @RequestBody body: UsersAchievementsEntity,
                authentication: Authentication) : ResultEntity<UsersAchievementsEntity> {
        try {
            verifyUserIdPermissionService.get(authentication, uid);
            val resultEmblema =  achievementsRepository.findAllByUid(body.idemblema!!)
            if (resultEmblema.isEmpty()){
                throw Exception("Emblema não encontrado")
            }
            val emblema = resultEmblema.first()
            if (emblema.categoria != "evento"){
                throw Exception("Este tipo de emblema não pode ser resgatado")
            }
            if (!emblema.disponivel!!){
                throw Exception("Emblema não disponível")
            }
            val result: List<UsersAchievementsEntity> = repository.findAllByUseridAndIdemblema(uid, emblema.uid!!)

            if (result.isNotEmpty()){
                throw Exception("Emblema já adquirido")
            }

            body.idemblema = emblema.uid!!
            body.timecria = Date().time
            body.userid = uid
            body.createdat = Date().time
            body.updatedat = Date().time
            body.uid = UUIDGenerator().generateId("").toString()

            val resultSave = repository.save(body)

            return ResultEntity(
                total = 0,
                status = StatusResultEnum.SUCCESS,
                data = listOf(resultSave),
                message = "Emblema adquirido com sucesso"
            )
        } catch (e: Exception) {
            return ResultEntity(
                total = 0,
                status = StatusResultEnum.ERROR,
                data = listOf(body),
                message = e.message
            )
        }
    }
}