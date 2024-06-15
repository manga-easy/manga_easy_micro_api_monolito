package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.LibrariesEntity
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
                repository.findByIduserAndUniqueid(
                    iduser = userAuth.userId,
                    uniqueid = uniqueId
                )
            } else {
                repository.findByIduser(
                    iduser = userAuth.userId,
                    pageable = PageRequest.of(offset ?: 0, limit ?: 25)
                )
            }
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @PutMapping
    @ResponseBody
    fun update(
        @RequestBody body: LibrariesEntity,
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        try {
            val result = repository.findByIduserAndUniqueid(
                iduser = body.iduser,
                uniqueid = body.uniqueid
            )
            if (result.isEmpty()) {
                repository.save(
                    body.copy(
                        uid = GetUidByFeature().get("users-libraries"),
                        createdat = Date().time,
                        updatedat = Date().time,
                        iduser = body.iduser
                    )
                )
            } else {
                val first = result.first()
                repository.save(
                    first.copy(
                        updatedat = body.updatedat,
                        status = body.status,
                        isdeleted = body.isdeleted,
                        manga = body.manga,
                        idhost = body.idhost,
                        uniqueid = body.uniqueid,
                    )
                )
            }
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(body),
                message = "Adicionado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

}