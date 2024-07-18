package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.dtos.BannerDtoV1
import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.repositories.BannersRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/banners")
@Tag(name = "Banners")
@Deprecated("Remover 0.18 -> 0.20")
class BannersControllerV1(@Autowired val repository: BannersRepository) {
    @GetMapping("/list")
    @ResponseBody
    fun list(
        @RequestParam status: String?,
        @RequestParam idhost: Int?
    ): ResultEntity {
        try {
            val result = repository.findAll()
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result.map { e ->
                    BannerDtoV1(
                        type = "",
                        updatedat = e.updatedAt,
                        createdat = e.createdAt,
                        uid = e.id,
                        image = e.image,
                        link = e.link,
                    )
                }.toList(),
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return HandleExceptions().handleCatch(e)
        }
    }
}