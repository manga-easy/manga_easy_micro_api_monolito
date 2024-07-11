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
class BannersController {
    @Autowired
    lateinit var repository: BannersRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
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
        val find = repository.findById(uid)
        if (!find.isPresent) {
            throw BusinessException("Banner não encontrado")
        }
        repository.deleteById(find.get().id!!)
    }

    @PostMapping("/v1")
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreateBannerDto
    ): BannersEntity {
        handleValidatorWrite(userAuth, body)
        return repository.save(
            BannersEntity(
                updatedAt = Date().time,
                link = body.link,
                image = body.image,
                createdAt = Date().time
            )
        )
    }

    @PutMapping("/v1/{id}")
    fun update(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreateBannerDto,
        @PathVariable id: String
    ): BannersEntity {
        handleValidatorWrite(userAuth, body)
        val find = repository.findById(id)
        if (!find.isPresent) {
            throw BusinessException("Banner não encontrado")
        }
        val banner = find.get().copy(
            updatedAt = Date().time,
            link = body.link,
            image = body.image
        )
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