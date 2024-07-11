package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.MessageService
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.RankingV1Dto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.earnXpDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.RankingRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.XpRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/level")
@Deprecated("Remover na 0.18.0 -> 0.20.0")
class LevelController {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var xpRepository: XpRepository

    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var rankingRepository: RankingRepository

    @Autowired
    lateinit var messageService: MessageService

    @Autowired
    lateinit var toggleService: ToggleService

    @GetMapping("/{userID}")
    @ResponseBody
    fun getXp(@AuthenticationPrincipal userAuth: UserAuth, @PathVariable userID: String): ResultEntity {
        return try {
            handlerPermissionUser.handleIsOwnerToken(userAuth, userID)
            val result = xpRepository.countXpTotalByUserId(userID)
            ResultEntity(listOf(result ?: 0))
        } catch (e: Exception) {
            HandleExceptions().handleCatch(e)
        }
    }

    @PutMapping("/{userID}/earn-xp")
    @ResponseBody
    fun earnXp(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable userID: String,
        @RequestBody body: earnXpDto,
    ): ResultEntity {
        return try {
            handlerPermissionUser.handleIsOwnerToken(userAuth, userID)
            val toggle = toggleService.getToggle<Boolean>(ToggleEnum.nivelAtivo)
            if (!toggle) {
                throw BusinessException("Nivel está desativado")
            }
            catalogRepository.findByUniqueid(body.uniqueID)
                ?: throw BusinessException("Manga não catalogado")
            messageService.sendXp(
                XpConsumerDto(
                    uniqueID = body.uniqueID,
                    chapterNumber = body.chapterNumber,
                    useId = userID,
                )
            )
            ResultEntity(listOf(false))
        } catch (e: Exception) {
            HandleExceptions().handleCatch(e)
        }
    }

    @GetMapping("/ranking")
    @ResponseBody
    fun getRanking(@RequestParam page: Int?): ResultEntity {
        return try {
            val result = rankingRepository.findAll(
                PageRequest.of(page ?: 0, 25)
                    .withSort(Sort.by("place"))
            )
            val listDto = result.map {
                RankingV1Dto(
                    name = it.name,
                    id = it.userId,
                    totalXp = it.totalXp,
                    place = it.place,
                    picture = it.picture
                )
            }.toList()
            ResultEntity(listDto)
        } catch (e: Exception) {
            HandleExceptions().handleCatch(e)
        }
    }
}