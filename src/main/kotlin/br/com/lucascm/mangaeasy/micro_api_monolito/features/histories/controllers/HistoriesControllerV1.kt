package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoryEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoryV1Dto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.repositories.HistoriesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/histories")
@Deprecated("Remover 0.18 -> 0.20")
@Tag(name = "Histories")
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
                val find = repository.findByUserIdAndUniqueId(
                    userId = userAuth.userId,
                    uniqueid = uniqueId
                )
                listOf(find)
            } else {
                repository.findByUserId(
                    userId = userAuth.userId,
                    pageable = PageRequest.of(offset ?: 0, limit ?: 25)
                )
            }
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result.filterNotNull().map { HistoryV1Dto.fromEntity(it) }.toList(),
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @PutMapping
    @ResponseBody
    fun update(
        @RequestBody body: HistoryV1Dto,
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
            val find = repository.findByUserIdAndUniqueId(
                userId = userAuth.userId,
                uniqueid = body.uniqueid
            )
            val result = if (find == null) {
                repository.save(
                    HistoryEntity(
                        updatedAt = Date().time,
                        createdAt = Date().time,
                        userId = userAuth.userId,
                        uniqueId = body.uniqueid,
                        chaptersRead = body.chapterlidos ?: "",
                        currentChapter = body.currentchapter,
                        isDeleted = body.isdeleted,
                        manga = body.manga,
                        catalogId = null,
                    )
                )
            } else {
                repository.save(
                    find.copy(
                        updatedAt = Date().time,
                        uniqueId = body.uniqueid,
                        chaptersRead = body.chapterlidos ?: "",
                        currentChapter = body.currentchapter,
                        isDeleted = body.isdeleted,
                        manga = body.manga
                    )
                )
            }
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(HistoryV1Dto.fromEntity(result)),
                message = "Adicionado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }
}