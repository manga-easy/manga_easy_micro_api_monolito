package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoriesEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoryV1Dto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.repositories.HistoriesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/histories")
@Deprecated("Remover 0.18 -> 0.20")
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
                data = result.map { HistoryV1Dto.fromEntity(it) }.toList(),
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
            val find = repository.findByUserIdAndUniqueid(
                userId = userAuth.userId,
                uniqueid = body.uniqueid
            )
            val result = if (find.isEmpty()) {
                repository.save(
                    HistoriesEntity(
                        updatedAt = Date().time,
                        createdAt = Date().time,
                        userId = userAuth.userId,
                        uniqueid = body.uniqueid,
                        chaptersRead = body.chapterlidos ?: "",
                        currentChapter = body.currentchapter,
                        isDeleted = body.isdeleted,
                        manga = body.manga
                    )
                )
            } else {
                val first = find.first()
                repository.save(
                    first.copy(
                        updatedAt = Date().time,
                        uniqueid = body.uniqueid,
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