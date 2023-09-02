package br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerUserAdmin
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.entities.SeasonsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.repositories.SeasonsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.Date

@RestController
@RequestMapping("/v1/seasons")
class SeasonsController(@Autowired val repository: SeasonsRepository) {
    @Autowired lateinit var getUidByFeature: GetUidByFeature
    @Autowired lateinit var handleExceptions: HandleExceptions
    @Autowired lateinit var handlerUserAdmin: HandlerUserAdmin
    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam status : String?,
             @RequestParam idhost : Int?
    ) : ResultEntity {
        try {
            val result: List<SeasonsEntity> = repository.findAll()
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
    @PostMapping()
    @ResponseBody
    fun create(
        @RequestBody body: SeasonsEntity,
        authentication: Authentication
    ) : ResultEntity {
        try {
            val isAdmin = handlerUserAdmin.get(authentication.principal.toString());
            if (!isAdmin){
                throw BusinessException("O usuario não tem permissão")
            }
            if (body.nome.isEmpty()){
                throw BusinessException("O nome não pode ser vazio")
            }
            val seasons = repository.findMaxNumber()
            var maxNumber = seasons
            val result = repository.save(body.copy(
                datainit = Date().time,
                updatedat = Date().time,
                createdat =  Date().time,
                uid = getUidByFeature.get("seasons"),
                number = ++maxNumber,
            ))
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Criado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }
    @PutMapping("/{uid}")
    @ResponseBody
    fun update(
        @RequestBody body: SeasonsEntity,
        authentication: Authentication,
        @PathVariable uid: String
    ) : ResultEntity {
        try {
            val isAdmin = handlerUserAdmin.get(authentication.principal.toString());
            if (!isAdmin){
                throw BusinessException("O usuario não tem permissão")
            }
            if (body.nome.isEmpty()){
                throw BusinessException("O nome não pode ser vazio")
            }
            val seasons = repository.findByUid(uid)
            if (seasons == null){
                throw BusinessException("Temporada não encontrada")
            }
            val result = repository.save(seasons.copy(
                nome = body.nome,
                updatedat = Date().time,
            ))
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Criado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }
}