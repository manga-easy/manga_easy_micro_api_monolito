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
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/banners")
@Tag(name = "Banners")
class BannersController {
    @Autowired
    lateinit var repository: BannersRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
    fun list(): List<BannersEntity> {
        return repository.findAll()
    }

    @GetMapping("/v1/{id}")
    fun getById(@PathVariable id: String): BannersEntity {
        return repository.findById(id).getOrNull()
            ?: throw BusinessException("Banner não encontrado")
    }

    @DeleteMapping("/v1/{id}")
    fun delete(
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable id: String
    ) {
        handlerPermissionUser.handleIsAdmin(userAuth)
        return repository.deleteById(id)
    }

    @PostMapping("/v1")
    fun create(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: CreateBannerDto
    ): BannersEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
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