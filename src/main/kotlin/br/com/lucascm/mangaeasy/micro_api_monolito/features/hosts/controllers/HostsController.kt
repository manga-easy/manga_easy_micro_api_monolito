package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos.CreateHostDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.HostsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@Tag(name = "Hosts")
@RequestMapping("/hosts")
class HostsController {
    @Autowired
    lateinit var repository: HostsRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
    @ResponseBody
    fun list(
        @RequestParam status: String?,
        @RequestParam isAll: Boolean = false,
        @RequestParam idhost: Int?
    ): List<HostsEntity> {
        if (isAll) {
            return repository.findAll()
        }
        if (idhost != null) {
            return repository.findByIdhost(idhost)
        }
        return repository.findByStatus(status ?: "enable")
    }


    @PostMapping
    @ResponseBody
    fun create(
        @RequestBody body: CreateHostDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): HostsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        handlerValidation(body)
        return repository.save(
            HostsEntity(
                updatedAt = Date().time,
                createdAt = Date().time,
                name = body.name,
                host = body.host,
                hostId = body.hostId,
                status = body.status,
                order = body.order
            )
        )
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun update(
        @RequestBody body: CreateHostDto,
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ): HostsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val find = repository.findByUid(id)
            ?: throw BusinessException("Host não encontrado")
        handlerValidation(body)
        return repository.save(
            find.copy(
                updatedAt = Date().time,
                name = body.name,
                host = body.host,
                hostId = body.hostId,
                status = body.status,
                order = body.order
            )
        )
    }

    private fun handlerValidation(body: CreateHostDto) {
        if (body.host.isEmpty()) {
            throw BusinessException("O campo host não pode ser vazio")
        }
        if (body.name.isEmpty()) {
            throw BusinessException("O campo name não pode ser vazio")
        }
        if (body.status.isEmpty()) {
            throw BusinessException("O campo status não pode ser vazio")
        }
        if (body.hostId == 0) {
            throw BusinessException("O campo hostId não pode ser 0")
        }
    }
}