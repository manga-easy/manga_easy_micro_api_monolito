package br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.dtos.ListStaffDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.entities.StaffEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.repositories.StaffRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.services.StaffService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/staff")
@Tag(name = "Staff")
class StaffController {
    companion object {
        private const val STAFF_NOT_FOUND_MESSAGE = "Membro da equipe não encontrado."
    }

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var staffRepository: StaffRepository

    @Autowired
    lateinit var staffService: StaffService

    @GetMapping("/v1")
    fun list(): List<ListStaffDto> {
        return staffService.findAll()
    }

    @GetMapping("/v1/{id}")
    fun listById(
        @PathVariable id: String
    ): StaffEntity {
        return staffRepository.findById(id).orElseThrow {
            BusinessException(STAFF_NOT_FOUND_MESSAGE)
        }
    }

    @GetMapping("/v1/users/{userId}")
    fun listByUserId(
        @PathVariable userId: String
    ): StaffEntity {
        return staffRepository.findByUserId(userId).orElseThrow {
            BusinessException(STAFF_NOT_FOUND_MESSAGE)
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
            throw BusinessException(STAFF_NOT_FOUND_MESSAGE)
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
            BusinessException(STAFF_NOT_FOUND_MESSAGE)
        }

        val updatedStaff = existingStaff.copy(
            type = body.type,
            userId = body.userId,
            updatedAt = System.currentTimeMillis()
        )

        return staffRepository.save(updatedStaff)
    }

}