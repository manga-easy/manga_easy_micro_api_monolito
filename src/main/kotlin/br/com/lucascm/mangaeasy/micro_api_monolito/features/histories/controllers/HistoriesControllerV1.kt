package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoriesEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.repositories.HistoriesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/histories")
class HistoriesControllerV1 {
    @Autowired
    lateinit var repository: HistoriesRepository

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
        @RequestBody body: HistoriesEntity,
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        try {
            val result = repository.findByIduserAndUniqueid(
                iduser = userAuth.userId,
                uniqueid = body.uniqueid
            )
            if (result.isEmpty()) {
                repository.save(
                    body.copy(
                        uid = GetUidByFeature().get("users-histories"),
                        createdat = Date().time,
                        updatedat = Date().time,
                        iduser = userAuth.userId
                    )
                )
            } else {
                val first = result.first()
                repository.save(
                    first.copy(
                        updatedat = body.updatedat,
                        capatual = body.capatual,
                        chapterlidos = body.chapterlidos,
                        currentchapter = body.currentchapter,
                        isdeleted = body.isdeleted,
                        manga = body.manga,
                        uniqueid = body.uniqueid
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