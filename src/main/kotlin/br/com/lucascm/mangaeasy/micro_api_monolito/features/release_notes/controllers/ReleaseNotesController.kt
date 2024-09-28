package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.dtos.ReleaseNotesDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.FeatureEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.FixEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.ReleaseNotesEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.repositories.ReleaseNotesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
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

    @GetMapping("/v1")
    fun list(): ReleaseNotesEntity? {
        return releaseNotesRepository.findFirstByOrderByCreatedAtDesc()
    }

    @GetMapping("/v1/all")
    fun listAll(@RequestParam page: Int?): Page<ReleaseNotesEntity> {
        return releaseNotesRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page ?: 0, 25))
    }

    @PostMapping("/v1")
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: ReleaseNotesDto
    ): ReleaseNotesEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val entity = mapDto(body)
        val existingReleaseNote = releaseNotesRepository.findByVersion(entity.version)
        if (existingReleaseNote != null) {
            throw BusinessException("Versão já tem nota de atualização")
        }
        return releaseNotesRepository.save(entity)
    }

    @PutMapping("/v1/{id}")
    fun update(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: ReleaseNotesDto,
        @PathVariable id: String
    ): ReleaseNotesEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)

        val releaseNoteDto = releaseNotesRepository.findById(id).getOrNull()
            ?: throw BusinessException("Nota de atualização não encontrada")
        val releaseNote = mapDtoToExistingEntity(body, releaseNoteDto)

        // Verificar se a versão está sendo alterada para uma que já existe
        val releaseNoteWithSameVersion = releaseNotesRepository.findByVersion(releaseNote.version)
        if (releaseNoteWithSameVersion != null && releaseNoteWithSameVersion.id != releaseNoteDto.id) {
            throw BusinessException("Outra nota de atualização está com essa versão!")
        }

        return releaseNotesRepository.save(releaseNote)
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


    private fun mapDto(body: ReleaseNotesDto): ReleaseNotesEntity {
        // Mapear DTO para entidade
        val releaseNotesEntity = ReleaseNotesEntity(
            version = body.version,
            description = body.description,
        )

        // Mapear Fixes
        val fixEntity = body.fixes.map { fixDTO ->
            FixEntity(
                title = fixDTO.title,
                subtitle = fixDTO.subtitle,
                description = fixDTO.description,
                releaseNotes = releaseNotesEntity
            )
        }.toMutableList()

        // Mapear Features
        val featureEntity = body.features.map { featureDTO ->
            FeatureEntity(
                title = featureDTO.title,
                subtitle = featureDTO.subtitle,
                description = featureDTO.description,
                releaseNotes = releaseNotesEntity
            )
        }.toMutableList()

        // Associar fixes e features à release note
        return releaseNotesEntity.copy(fixes = fixEntity, features = featureEntity)
    }

    private fun mapDtoToExistingEntity(
        body: ReleaseNotesDto,
        existingEntity: ReleaseNotesEntity
    ): ReleaseNotesEntity {
        // Atualizar os campos básicos
        val updatedEntity = existingEntity.copy(
            version = body.version,
            description = body.description
        )

        // Mapear e atualizar Fixes
        val fixEntities = body.fixes.map { fixDTO ->
            FixEntity(
                title = fixDTO.title,
                subtitle = fixDTO.subtitle,
                description = fixDTO.description,
                releaseNotes = updatedEntity
            )
        }.toMutableList()

        // Mapear e atualizar Features
        val featureEntities = body.features.map { featureDTO ->
            FeatureEntity(
                title = featureDTO.title,
                subtitle = featureDTO.subtitle,
                description = featureDTO.description,
                releaseNotes = updatedEntity
            )
        }.toMutableList()

        // Associar os novos fixes e features
        return updatedEntity.copy(fixes = fixEntities, features = featureEntities)
    }
}