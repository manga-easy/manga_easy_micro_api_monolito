package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.MessageService
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.earnXpDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.XpRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/{userId}/experiences")
@Tag(name = "Levels")
class XpController {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var xpRepository: XpRepository

    @Autowired
    lateinit var messageService: MessageService

    @Autowired
    lateinit var toggleService: ToggleService

    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @GetMapping("/v1")
    @ResponseBody
    fun getXp(@AuthenticationPrincipal userAuth: UserAuth, @PathVariable userId: String): Long {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        val result = xpRepository.countXpTotalByUserId(userId)
        return result ?: 0
    }

    @PutMapping("/v1/earn-xp")
    @ResponseBody
    fun earnXp(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable userId: String,
        @RequestBody body: earnXpDto,
    ) {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userId)
        val toggle = toggleService.getToggle(ToggleEnum.nivelAtivo).toBoolean()
        if (!toggle) {
            throw BusinessException("Nível está desativado")
        }
        catalogRepository.findByUniqueid(body.uniqueID)
            ?: throw BusinessException("Mangá não catalogado")
        messageService.sendXp(
            XpConsumerDto(
                uniqueID = body.uniqueID,
                chapterNumber = body.chapterNumber,
                userId = userId,
            )
        )

    }
}