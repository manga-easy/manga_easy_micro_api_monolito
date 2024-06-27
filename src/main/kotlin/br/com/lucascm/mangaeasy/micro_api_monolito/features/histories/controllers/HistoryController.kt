package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoriesEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.UpdateHistoryDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.repositories.HistoriesRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/history")
@Tag(name = "History")
class HistoryController {
    @Autowired
    lateinit var repository: HistoriesRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
    fun list(
        @RequestParam uniqueId: String?,
        @RequestParam limit: Int?,
        @RequestParam offset: Int?,
        @AuthenticationPrincipal userAuth: UserAuth
    ): List<HistoriesEntity> {
        return if (uniqueId != null) {
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

    }

    @PutMapping("/v1")
    fun update(
        @RequestBody body: UpdateHistoryDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): HistoriesEntity {
        val result = repository.findByUserIdAndUniqueid(
            userId = userAuth.userId,
            uniqueid = body.uniqueid
        )
        return if (result.isEmpty()) {
            repository.save(
                HistoriesEntity(
                    updatedAt = Date().time,
                    createdAt = Date().time,
                    userId = userAuth.userId,
                    uniqueid = body.uniqueid,
                    chaptersRead = body.chaptersRead,
                    currentChapter = body.currentChapter,
                    isDeleted = body.hasDeleted,
                    manga = body.manga
                )
            )
        } else {
            val first = result.first()
            repository.save(
                first.copy(
                    updatedAt = Date().time,
                    uniqueid = body.uniqueid,
                    chaptersRead = body.chaptersRead,
                    currentChapter = body.currentChapter,
                    isDeleted = body.hasDeleted,
                    manga = body.manga
                )
            )
        }

    }
}