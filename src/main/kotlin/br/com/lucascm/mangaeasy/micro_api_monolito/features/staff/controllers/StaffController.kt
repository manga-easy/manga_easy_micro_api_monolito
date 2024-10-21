package br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.entities.ReleaseNoteEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.entities.StaffEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.repositories.StaffRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Optional
import kotlin.jvm.optionals.getOrNull
import kotlin.jvm.optionals.toList

@RestController
@RequestMapping("/staff")
@Tag(name = "Staff")
class StaffController {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var staffRepository: StaffRepository

    @GetMapping("/v1")
    fun list(): List<StaffEntity> {
        return staffRepository.findAll()
    }

    @GetMapping("/v1/{id}")
    fun listById(
        @PathVariable id: String
    ): StaffEntity {
        return staffRepository.findById(id).orElseThrow {
            BusinessException("Membro da equipe não encontrado.")
        }
    }

    @GetMapping("/v1/users/{userId}")
    fun listByUserId(
        @PathVariable userId: String
    ): StaffEntity {
        return staffRepository.findByUserId(userId).orElseThrow {
            BusinessException("Membro da equipe não encontrado para o userId fornecido.")
        }
    }

    @PostMapping("/v1")
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: StaffEntity
    ): StaffEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)

        val staffHasRole = staffRepository.findByUserId(body.userId)

        if (staffHasRole.isPresent) {
            throw BusinessException("Usuário já possui um cargo.")
        }

        return staffRepository.save(body)
    }

    @DeleteMapping("/v1/{userId}")
    fun delete(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable userId: String
    ) {
        handlerPermissionUser.handleIsAdmin(userAuth)

        val staffEntity = staffRepository.findByUserId(userId)

        if (staffEntity.isEmpty) {
            throw BusinessException("Membro da equipe não encontrado.")
        }

        return staffRepository.delete(staffEntity.get())
    }

    @PutMapping("/v1/{id}")
    fun update(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: StaffEntity,
        @PathVariable id: String
    ): StaffEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)

        val existingStaff = staffRepository.findByUserId(body.userId).orElseThrow {
            BusinessException("Membro da equipe não encontrado.")
        }

        val updatedStaff = existingStaff.copy(
            type = body.type,
            userId = body.userId,
            updatedAt = System.currentTimeMillis()
        )

        return staffRepository.save(updatedStaff)
    }

}