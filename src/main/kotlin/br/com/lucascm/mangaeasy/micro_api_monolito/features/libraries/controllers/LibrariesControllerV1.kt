package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.LibrariesV1Dto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.UpdateLibraryDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.services.LibraryService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/libraries")
@Deprecated("Remover 0.18 -> 0.20")
@Tag(name = "Libraries")
class LibrariesControllerV1 {
    @Autowired
    lateinit var libraryService: LibraryService

    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping
    @ResponseBody
    fun list(
        @RequestParam uniqueId: String?,
        @RequestParam limit: Int?,
        @RequestParam offset: Int?,
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        try {
            val result = if (uniqueId != null) {
                val find = libraryService.findByUserIdAndUniqueId(
                    userId = userAuth.userId,
                    uniqueId = uniqueId
                )
                listOf(find)
            } else {
                libraryService.findByUserId(
                    userId = userAuth.userId,
                    pageable = PageRequest.of(offset ?: 0, limit ?: 25)
                )
            }
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result.filterNotNull().map { LibrariesV1Dto.fromEntity(it) }.toList(),
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @PutMapping
    @ResponseBody
    fun update(
        @RequestBody body: LibrariesV1Dto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        try {
            if (body.uniqueid.isEmpty()) {
                throw BusinessException("UniqueId não pode ser vazio")
            }
            if (body.iduser.isEmpty()) {
                body.iduser = userAuth.userId
            }
            handlerPermissionUser.handleIsOwnerToken(userAuth, body.iduser)
            val find = libraryService.findByUserIdAndUniqueId(
                userId = body.iduser,
                uniqueId = body.uniqueid
            )
            val result = if (find == null) {
                libraryService.create(
                    userAuth.userId,
                    UpdateLibraryDto(
                        hostId = body.idhost ?: 0,
                        hasDeleted = body.isdeleted,
                        manga = body.manga!!,
                        status = body.status!!
                    )
                )
            } else {
                libraryService.update(
                    find.id!!,
                    UpdateLibraryDto(
                        status = body.status!!,
                        hasDeleted = body.isdeleted,
                        manga = body.manga!!,
                        hostId = body.idhost ?: 0
                    )
                )
            }
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(LibrariesV1Dto.fromEntity(result)),
                message = "Adicionado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

}