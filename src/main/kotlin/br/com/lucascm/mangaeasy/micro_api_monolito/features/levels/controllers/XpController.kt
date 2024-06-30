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
@RequestMapping("/xp")
@Tag(name = "Xp")
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

    @GetMapping("/v1/{userID}")
    @ResponseBody
    fun getXp(@AuthenticationPrincipal userAuth: UserAuth, @PathVariable userID: String): Long {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userID)
        val result = xpRepository.countXpTotalByUserId(userID)
        return result ?: 0
    }

    @PutMapping("/v1/{userID}/earn-xp")
    @ResponseBody
    fun earnXp(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable userID: String,
        @RequestBody body: earnXpDto,
    ) {
        handlerPermissionUser.handleIsOwnerToken(userAuth, userID)
        val toggle = toggleService.getToggle<Boolean>(ToggleEnum.nivelAtivo)
        if (!toggle) {
            throw BusinessException("Nível está desativado")
        }
        catalogRepository.findByUniqueid(body.uniqueID)
            ?: throw BusinessException("Mangá não catalogado")
        messageService.sendXp(
            XpConsumerDto(
                uniqueID = body.uniqueID,
                chapterNumber = body.chapterNumber,
                useId = userID,
            )
        )

    }
}