package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.dtos.ReleaseNotesDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.dtos.UpdateReleaseNotesDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.ReleaseNotesEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.repositories.ReleaseNotesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/release-notes")
@Tag(name = "ReleaseNotes")
class ReleaseNotesController {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var releaseNotesRepository: ReleaseNotesRepository

    @GetMapping("/v1/version/{version}")
    fun list(
        @PathVariable version: String
    ): ReleaseNotesEntity {
        return releaseNotesRepository.findByVersion(version) ?:  throw BusinessException("Versão não encontrada")
    }

    @GetMapping("/v1")
    fun list(@RequestParam page: Int?): List<ReleaseNotesEntity> {
        return releaseNotesRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page ?: 0, 25))
    }

    @PostMapping("/v1")
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: ReleaseNotesDto
    ): ReleaseNotesEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val existingReleaseNote = releaseNotesRepository.findByVersion(body.version)
        if (existingReleaseNote != null) {
            throw BusinessException("Versão já tem nota de atualização")
        }
        return releaseNotesRepository.save(body.toEntity())
    }

    @PutMapping("/v1/{id}")
    fun update(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: UpdateReleaseNotesDto,
        @PathVariable id: String
    ): ReleaseNotesEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val releaseNote = releaseNotesRepository.findById(id).getOrNull()
            ?: throw BusinessException("Nota de atualização não encontrada")

        return releaseNotesRepository.save(
            releaseNote.copy(
                features = body.features,
                fixes = body.fixes,
                description = body.description,
            )
        )
    }

    @DeleteMapping("/v1/{id}")
    fun delete(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable id: String
    ) {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val releaseNote = releaseNotesRepository.findById(id).getOrNull()
            ?: throw BusinessException("Nota de atualização não encontrada")
        return releaseNotesRepository.delete(releaseNote)
    }
}