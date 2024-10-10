package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.dtos.ReleaseNoteDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.entities.ReleaseNoteEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.repositories.ReleaseNoteRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/release-note")
@Tag(name = "Release Note")
class ReleaseNoteController {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var releaseNoteRepository: ReleaseNoteRepository

    @GetMapping("/v1/version/{version}")
    fun findByVersion(
        @PathVariable version: String
    ): ReleaseNoteEntity {
        return releaseNoteRepository.findByVersion(version) ?: throw BusinessException("Versão não encontrada")
    }

    @GetMapping("/v1/{id}")
    fun findById(
        @PathVariable id: String
    ): ReleaseNoteEntity {
        return releaseNoteRepository.findById(id).getOrNull() ?: throw BusinessException("Versão não encontrada")
    }

    @GetMapping("/v1")
    fun list(@RequestParam page: Int?): List<ReleaseNoteEntity> {
        return releaseNoteRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page ?: 0, 25))
    }

    @PostMapping("/v1")
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: ReleaseNoteDto
    ): ReleaseNoteEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val existingReleaseNote = releaseNoteRepository.findByVersion(body.version)
        if (existingReleaseNote != null) {
            throw BusinessException("Versão já tem nota de atualização")
        }
        return releaseNoteRepository.save(body.toEntity())
    }

    @PutMapping("/v1/{id}")
    fun update(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: ReleaseNoteDto,
        @PathVariable id: String
    ): ReleaseNoteEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val existingReleaseNote = releaseNoteRepository.findByVersion(body.version)
        if (existingReleaseNote != null && existingReleaseNote.version != body.version) {
            throw BusinessException("Versão já tem nota de atualização")
        }
        val releaseNote = releaseNoteRepository.findById(id).getOrNull()
            ?: throw BusinessException("Nota de atualização não encontrada")

        return releaseNoteRepository.save(
            releaseNote.copy(
                version = body.version,
                description = body.description,
                features = body.features,
                fixes = body.fixes,
            )
        )
    }

    @DeleteMapping("/v1/{id}")
    fun delete(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable id: String
    ) {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val releaseNote = releaseNoteRepository.findById(id).getOrNull()
            ?: throw BusinessException("Nota de atualização não encontrada")
        return releaseNoteRepository.delete(releaseNote)
    }
}
