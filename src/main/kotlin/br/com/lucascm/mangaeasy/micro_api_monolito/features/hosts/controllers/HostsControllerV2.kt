package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerUserAdmin
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.HostsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.Date


@RestController
@RequestMapping("/v2/hosts")
class HostsControllerV2(@Autowired val repository: HostsRepository) {
    @Autowired lateinit var handleExceptions: HandleExceptions<HostsEntity>
    @Autowired lateinit var handlerUserAdmin: HandlerUserAdmin
    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam status : String?,
             @RequestParam isAll: Boolean = false,
             @RequestParam idhost : Int?
    ) : ResultEntity<HostsEntity> {
        try {
            val result = handlerFilters(status, isAll, idhost)
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
    private fun handlerFilters(
        status : String?,
        isAll: Boolean = false,
        idhost : Int?
    ): List<HostsEntity> {
        if(isAll){
            return repository.findAll()
        }
        if(idhost != null) {
            return repository.findByIdhost(idhost)
        }
        return repository.findByStatus(status ?: "enable")
    }
    @PostMapping
    @ResponseBody
    fun create(@RequestBody body: HostsEntity,
               authentication: Authentication
    ): ResultEntity<HostsEntity>{
        try {
            handlerUserAdmin.handleIsAdmin(authentication.principal.toString())
            handlerValidation(body)
            val result = repository.save(body.copy(
                updatedat = Date().time,
                createdat = Date().time,
                uid = GetUidByFeature().get("hosts")
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
    fun update(@RequestBody body: HostsEntity,
               @PathVariable uid: String,
               authentication: Authentication
    ): ResultEntity<HostsEntity>{
        try {
            handlerUserAdmin.handleIsAdmin(authentication.principal.toString())
            val resultfind = repository.findByUid(uid)
            if (resultfind == null){
                throw BusinessException("Host não encontrado")
            }
            handlerValidation(body)
            val result = repository.save(resultfind.copy(
                status = body.status,
                name = body.name,
                host = body.host,
                idhost = body.idhost,
                order = body.order,
                interstitialadunitid = body.interstitialadunitid,
                updatedat = Date().time,
            ))
            return  ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Criado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    private fun handlerValidation(body: HostsEntity){
        if (body.host.isEmpty()){
            throw BusinessException("O campo host não pode ser vazio")
        }
        if (body.name.isEmpty()){
            throw BusinessException("O campo name não pode ser vazio")
        }
        if (body.interstitialadunitid.isEmpty()){
            throw BusinessException("O campo interstitialadunitid não pode ser vazio")
        }
        if (body.status.isEmpty()){
            throw BusinessException("O campo status não pode ser vazio")
        }
    }
}