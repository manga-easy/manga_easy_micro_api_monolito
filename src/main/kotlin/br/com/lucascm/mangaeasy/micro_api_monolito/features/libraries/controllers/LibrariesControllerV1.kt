package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.LibrariesEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.LibrariesV1Dto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.repositories.LibrariesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/libraries")
class LibrariesControllerV1 {
    @Autowired
    lateinit var repository: LibrariesRepository

    @Autowired
    lateinit var handleExceptions: HandleExceptions

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
                repository.findByUserIdAndUniqueid(
                    userId = userAuth.userId,
                    uniqueid = uniqueId
                )
            } else {
                repository.findByUserId(
                    userId = userAuth.userId,
                    pageable = PageRequest.of(offset ?: 0, limit ?: 25)
                )
            }
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result.map { LibrariesV1Dto.fromEntity(it) }.toList(),
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
            val find = repository.findByUserIdAndUniqueid(
                userId = body.iduser,
                uniqueid = body.uniqueid
            )
            val result = if (find.isEmpty()) {
                repository.save(
                    LibrariesEntity(
                        createdAt = Date().time,
                        updatedAt = Date().time,
                        userId = userAuth.userId,
                        hostId = body.idhost ?: 0,
                        hasDeleted = body.isdeleted,
                        manga = body.manga!!,
                        uniqueid = body.uniqueid,
                        status = body.status!!
                    )
                )
            } else {
                val first = find.first()
                repository.save(
                    first.copy(
                        updatedAt = Date().time,
                        status = body.status!!,
                        hasDeleted = body.isdeleted,
                        manga = body.manga!!,
                        hostId = body.idhost!!,
                        uniqueid = body.uniqueid
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