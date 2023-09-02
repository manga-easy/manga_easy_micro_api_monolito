package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerUserAdmin
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos.HostDtoV1
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.HostsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.Date


@RestController
@RequestMapping("/v1/hosts")
class HostsControllerV1(@Autowired val repository: HostsRepository) {
    @Autowired lateinit var handleExceptions: HandleExceptions
    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam status : String?,
             @RequestParam isAll: Boolean = false,
             @RequestParam idhost : Int?
    ) : ResultEntity {
        try {
            val result = handlerFilters(status, isAll, idhost)
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result.map{e-> HostDtoV1(
                    status = e.status,
                    name = e.name,
                    _uid = e.uid,
                    _createdat = e.createdat,
                    host = e.host,
                    _updatedat = e.updatedat,
                    order = e.order,
                    interstitialadunitid = e.interstitialadunitid,
                    idhost = e.idhost,
                )}.toList(),
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

}