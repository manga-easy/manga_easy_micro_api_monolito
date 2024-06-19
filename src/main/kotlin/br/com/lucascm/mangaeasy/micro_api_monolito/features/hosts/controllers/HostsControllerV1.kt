package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos.HostDtoV1
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.HostsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/hosts")
@Deprecated("Remover 0.18 -> 0.20")
class HostsControllerV1(@Autowired val repository: HostsRepository) {
    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @GetMapping("/list")
    @ResponseBody
    fun list(
        @RequestParam status: String?,
        @RequestParam isAll: Boolean = false,
        @RequestParam idhost: Int?
    ): ResultEntity {
        try {
            val result = handlerFilters(status, isAll, idhost)
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result.map { e ->
                    HostDtoV1(
                        status = e.status,
                        name = e.name,
                        _uid = e.id,
                        _createdat = e.createdAt,
                        host = e.urlApi,
                        _updatedat = e.updatedAt,
                        order = e.order,
                        interstitialadunitid = "",
                        idhost = e.hostId,
                    )
                }.toList(),
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    private fun handlerFilters(
        status: String?,
        isAll: Boolean = false,
        idhost: Int?
    ): List<HostsEntity> {
        if (isAll) {
            return repository.findAll()
        }
        if (idhost != null) {
            return repository.findByHostId(idhost)
        }
        return repository.findByStatus(status ?: "enable")
    }

}