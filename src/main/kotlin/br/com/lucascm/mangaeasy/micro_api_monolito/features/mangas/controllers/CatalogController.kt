package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/catalog")
class CatalogController {
    @Autowired
    lateinit var catalogRepository: CatalogRepository
    @GetMapping("/search")
    @ResponseBody
    fun search(@RequestParam genres: String,
               @RequestParam search: String?,
               @RequestParam author: String?,
               @RequestParam offset: Int?,
               @RequestParam uniqueid: String?,
               @RequestParam limit: Int?,
               @RequestParam yearAt: Int?,
               @RequestParam yearFrom: Int?
    ): ResultEntity {
        val result = catalogRepository.list(
            genres = genres.split("<>"),
            search = search,
            author = author,
            isAdult = false,
            pageable = PageRequest.of(offset ?: 0, limit?: 25),
            uniqueid = uniqueid,
            yearAt = yearAt,
            yearFrom = yearFrom
        )
        return ResultEntity(
           message = "Sucesso",
           total = result.totalPages,
           data = result.content,
           status = StatusResultEnum.SUCCESS,
        )
    }
}