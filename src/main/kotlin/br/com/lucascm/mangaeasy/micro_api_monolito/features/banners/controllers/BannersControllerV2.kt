package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.dtos.CreateBannerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.entities.BannersEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.repositories.BannersRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Tag(name = "Banners")
@RequestMapping("/banners")
class BannersControllerV2 {
    @Autowired
    lateinit var repository: BannersRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1/list")
    fun list(
        @RequestParam status: String?,
        @RequestParam idhost: Int?
    ): List<BannersEntity> {
        return repository.findAll()
    }

    @DeleteMapping("/v1/{uid}")
    fun delete(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable uid: String
    ) {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val result = repository.findByUid(uid)
            ?: throw BusinessException("Banner não encontrado")
        repository.delete(result)
    }

    @PostMapping("/v1/")
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreateBannerDto
    ): BannersEntity {
        handleValidatorWrite(userAuth, body)
        return repository.save(
            BannersEntity(
                updatedat = Date().time,
                link = body.link,
                image = body.image,
                createdat = Date().time
            )
        )
    }

    @PutMapping("/v1/{uid}")
    fun update(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreateBannerDto,
        @PathVariable uid: String
    ): BannersEntity {
        handleValidatorWrite(userAuth, body)
        val find = repository.findByUid(uid)
            ?: throw BusinessException("Banner não encontrado")
        val banner = find.apply {
            updatedat = Date().time
            link = body.link
            image = body.image
        }
        return repository.save(banner)

    }

    private fun handleValidatorWrite(@AuthenticationPrincipal userAuth: UserAuth, body: CreateBannerDto) {
        handlerPermissionUser.handleIsAdmin(userAuth)

        if (body.image.isEmpty()) {
            throw BusinessException("Campo Image não pode ser vazio")
        }
        if (body.link.isEmpty()) {
            throw BusinessException("Campo link não pode ser vazio")
        }
    }
}