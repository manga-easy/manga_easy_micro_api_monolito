package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.BannersService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.dtos.CreateBannerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.entities.BannersEntity
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/banners")
@Tag(name = "Banners")
class BannersController {
    @Autowired
    lateinit var bannersService: BannersService

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
    fun list(@RequestParam page: Int?): List<BannersEntity> {
        return bannersService.findAll(page ?: 0)
    }

    @GetMapping("/v1/{id}")
    fun getById(@PathVariable id: String): BannersEntity {
        return bannersService.findById(id)
    }

    @DeleteMapping("/v1/{id}")
    fun delete(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable id: String
    ) {
        handlerPermissionUser.handleIsAdmin(userAuth)
        return
    }

    @PostMapping("/v1")
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreateBannerDto
    ): BannersEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        return bannersService.create(body)

    }

    @PutMapping("/v1/{id}")
    fun update(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreateBannerDto,
        @PathVariable id: String
    ): BannersEntity {
        handleValidatorWrite(userAuth, body)
        return bannersService.update(body, id)

    }

    @PutMapping("/v1/{id}/images")
    fun uploadImage(
        @RequestPart file: MultipartFile,
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth,
    ): BannersEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        return bannersService.updateImage(id, file)
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