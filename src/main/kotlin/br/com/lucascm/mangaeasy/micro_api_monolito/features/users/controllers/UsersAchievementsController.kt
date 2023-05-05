package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.AchievementsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersAchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.Date

@RestController
@RequestMapping("/v1/users")
class UsersAchievementsController(@Autowired val repository: UsersAchievementsRepository, @Autowired val achievementsRepository: AchievementsRepository) {
    @GetMapping("/{uid}/achievements")
    @ResponseBody
    fun listAchievements(@PathVariable uid: String) : ResultEntity<UsersAchievementsEntity> {
        try {
            val result: List<UsersAchievementsEntity> = repository.findAllByUserid(uid)
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
    fun acquire(@PathVariable uid: String, @RequestBody body: UsersAchievementsEntity) : ResultEntity<UsersAchievementsEntity> {
        try {
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
            val result: List<UsersAchievementsEntity> = repository.findAllByUseridAndIdemblema(uid, emblema._uid!!)

            if (result.isNotEmpty()){
                throw Exception("Emblema já adquirido")
            }

            body.idemblema = emblema._uid!!
            body.timecria = Date().time
            body.userid = uid
            body._createdat = Date().time
            body._updatedat = Date().time
            body._uid = UUIDGenerator().generateId("").toString()

            val resultSave = repository.save(body)

            return ResultEntity(
                total = 0,
                status = StatusResultEnum.SUCCESS,
                data = listOf(resultSave),
                message = "Listado com sucesso"
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