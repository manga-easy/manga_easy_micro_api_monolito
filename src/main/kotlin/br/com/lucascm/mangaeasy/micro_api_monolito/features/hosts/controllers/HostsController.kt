package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos.CreateHostDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.HostsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/hosts")
@Tag(name = "Hosts")
class HostsController {
    @Autowired
    lateinit var repository: HostsRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
    fun list(
        @RequestParam status: String?,
        @RequestParam page: Int?,
        @RequestParam name: String?,
        @RequestParam hostId: Int?
    ): List<HostsEntity> {
        return repository.findAll(
            Specification(fun(
                root: Root<HostsEntity>,
                _: CriteriaQuery<*>,
                builder: CriteriaBuilder,
            ): Predicate? {
                val predicates = mutableListOf<Predicate>()
                if (hostId != null) {
                    predicates.add(
                        builder.equal(
                            root.get<String>(HostsEntity::hostId.name),
                            hostId
                        )
                    )
                }
                if (status != null) {
                    predicates.add(
                        builder.equal(
                            root.get<String>(HostsEntity::status.name),
                            status
                        )
                    )
                }
                if (name != null) {
                    predicates.add(
                        builder.like(
                            builder.lower(root.get(HostsEntity::name.name)),
                            "%" + name.lowercase() + "%"
                        )
                    )
                }
                return builder.and(*predicates.toTypedArray())
            }),
            PageRequest.of(
                page ?: 0, 25, Sort.by(
                    Sort.Direction.DESC,
                    HostsEntity::order.name
                )
            )
        ).content
    }

    @GetMapping("/v1/{id}")
    fun getById(@PathVariable id: String): HostsEntity {
        return repository.findById(id).getOrNull()
            ?: throw BusinessException("Host não encontrado")
    }


    @PostMapping("/v1")
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
                urlApi = body.urlApi,
                hostId = body.hostId,
                status = body.status,
                order = body.order
            )
        )
    }

    @PutMapping("/v1/{id}")
    fun update(
        @RequestBody body: CreateHostDto,
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ): HostsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val find = repository.findById(id)
        if (!find.isPresent) {
            throw BusinessException("Host não encontrado")
        }
        handlerValidation(body)
        return repository.save(
            find.get().copy(
                updatedAt = Date().time,
                name = body.name,
                urlApi = body.urlApi,
                hostId = body.hostId,
                status = body.status,
                order = body.order
            )
        )
    }

    private fun handlerValidation(body: CreateHostDto) {
        if (body.urlApi.isEmpty()) {
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