package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.services.CatalogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/catalog")
class CatalogController {
    @Autowired
    lateinit var catalogService: CatalogService
    @GetMapping
    @ResponseBody
    fun search(@RequestParam genres: String?,
               @RequestParam search: String?,
               @RequestParam author: String?,
               @RequestParam page: Int?,
               @RequestParam uniqueid: String?,
               @RequestParam limit: Int?,
               @RequestParam yearAt: Int?,
               @RequestParam yearFrom: Int?,
               @RequestParam scans: String?,
               @RequestParam isAdult: Boolean?
    ): ResultEntity {
        val result = catalogService.list(
            genres = genres?.split("<>") ?: listOf(),
            search = search,
            author = author,
            isAdult = isAdult ?: false,
            limit = limit,
            page = page,
            uniqueid = uniqueid,
            yearAt = yearAt,
            yearFrom = yearFrom,
            scans = scans,
        )
        return ResultEntity(
           message = "Sucesso",
           total = result.content.size,
           data = result.content,
           status = StatusResultEnum.SUCCESS,
        )
    }
}