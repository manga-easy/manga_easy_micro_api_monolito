package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.VerifyUserIdPermissionService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.entities.SeasonsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.repositories.SeasonsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersLevelsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersLevelsRepository
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/users")
class UsersLevelsController(@Autowired val repository: UsersLevelsRepository,
                            @Autowired val verifyUserIdPermissionService: VerifyUserIdPermissionService,
                            @Autowired val seasonsRepository: SeasonsRepository,
                            val logger: KotlinLogging = KotlinLogging
) {
    @GetMapping("/{uid}/levels")
    @ResponseBody
    fun list(@PathVariable uid: String,
             @RequestParam idSeason: String?,
             authentication: Authentication) : ResultEntity<UsersLevelsEntity> {
        try {
            verifyUserIdPermissionService.get(authentication, uid)
            var season = idSeason ?: seasonsRepository.findTop1ByOrderByNumberDesc().uid!!
            val result = repository.findByTemporadaAndUserid(season, uid)
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
    @PutMapping("/{uid}/levels")
    @ResponseBody
    fun updateLevel(@PathVariable uid: String,
                    @RequestBody levelApp: UsersLevelsEntity?,
                    authentication: Authentication): ResultEntity<UsersLevelsEntity>  {
        try {
            verifyUserIdPermissionService.get(authentication, uid)
            val seasonCurret = seasonsRepository.findTop1ByOrderByNumberDesc()
            //carrega os dados da nuvem
            val levelDataBase = repository.findByTemporadaAndUserid(seasonCurret.uid!!, uid)
            //1 case cria nivel
            if (levelApp == null && levelDataBase.isEmpty()) {
                return createLevel(seasonCurret.uid!!, uid)
            }
            //se for de temporada diferente da atual cria um nivel novo
            if (levelApp != null) {
                if (levelApp.temporada != seasonCurret.uid) {
                    saveLevel(levelApp, levelDataBase.first())
                    return createLevel(seasonCurret.uid!!, uid)
                }
            }
            //2 caso existe na nuvem mas não exite no local
            if (levelApp == null && levelDataBase.isNotEmpty()) {
                return ResultEntity(
                    StatusResultEnum.SUCCESS,
                    "Nivel atual",
                    1,
                    listOf(levelDataBase.first())
                )
            }
            //3 caso existe local mas não exite na nuvem
            if (levelApp != null && levelDataBase.isEmpty()) {
                throw Exception("Nivel não exite no Banco")
            }
            //4 caso existe nois então verifica qual é mais e atualiza o maior
            if (levelApp != null && levelDataBase.isNotEmpty()) {
               var result = saveLevel(levelApp, levelDataBase.first())
                return ResultEntity(
                    StatusResultEnum.SUCCESS,
                    "Nivel sincronizado com sucesso",
                    1,
                    listOf(result)
                )
            }
            throw Exception("Nenhum condição foi atendida: $levelApp")
        } catch (e: Exception) {
            logger.logger("levels-put").info(e.stackTrace.toString())
            return  ResultEntity(
                StatusResultEnum.ERROR,
                e.message,
                1,
                listOf()
            )
        }
    }

    private fun returnLevelSuperior(a: UsersLevelsEntity, b: UsersLevelsEntity): UsersLevelsEntity {
        val propertyA = a.lvl!!
        val propertyB = b.lvl!!
        return if (propertyA > propertyB) {
            a
        } else if (propertyA < propertyB) {
            b
        } else {
            val protA = a.quantity!!
            val protB = b.quantity!!
            if (protA > protB) {
                a
            } else if (protA < protB) {
                b
            } else {
                b
            }
        }
    }
   private fun createLevel(idSeason: String, userId: String): ResultEntity<UsersLevelsEntity>{
        var levelNew = UsersLevelsEntity()
        levelNew.lvl = 0
        levelNew.quantity = 0
        levelNew.temporada = idSeason
        levelNew.minute = 0
        levelNew.total = 0
        levelNew.timecria = Date().time
        levelNew.timeup = Date().time
        levelNew.updatedat = Date().time
        levelNew.createdat = Date().time
        levelNew.userid = userId
        levelNew.uid = ObjectIdGenerators.UUIDGenerator().generateId("").toString()
        repository.save(levelNew)
       return ResultEntity(
           total = 1,
           status = StatusResultEnum.SUCCESS,
           data = listOf(levelNew),
           message = "Nivel criado com sucesso"
       )
   }
   fun saveLevel(levelApp: UsersLevelsEntity, levelDataBase: UsersLevelsEntity): UsersLevelsEntity{
       var levelCurret = returnLevelSuperior(levelApp, levelDataBase)
       levelCurret.id = levelDataBase.id
       levelCurret.uid = levelDataBase.uid
       levelCurret.timeup = Date().time
       levelCurret.updatedat = Date().time
       repository.save(levelCurret)
       return levelCurret
   }
}