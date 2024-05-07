package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.XpRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.services.CatalogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/catalog")
class CatalogController {
    @Autowired
    lateinit var catalogService: CatalogService

    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var xpRepository: XpRepository

    @GetMapping
    @ResponseBody
    fun search(
        @RequestParam genres: String?,
        @RequestParam search: String?,
        @RequestParam author: String?,
        @RequestParam page: Int?,
        @RequestParam uniqueid: String?,
        @RequestParam limit: Int?,
        @RequestParam yearAt: Int?,
        @RequestParam yearFrom: Int?,
        @RequestParam scans: String?,
        @RequestParam isAdult: Boolean?,
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

    @GetMapping("/over-18")
    @ResponseBody
    fun over18(): ResultEntity {
        val result = catalogService.list(
            genres = listOf("adult"),
            isAdult = true,
            limit = 20000,
        )
        return ResultEntity(
            message = "Sucesso",
            total = result.content.size,
            data = result.content.map { it.uniqueid }.toList(),
            status = StatusResultEnum.SUCCESS,
        )
    }

    @GetMapping("/random")
    @ResponseBody
    fun random(@RequestParam isAdult: Boolean?): ResultEntity {
        val result = catalogRepository.findMangaRandom(isAdult ?: false)
        return ResultEntity(listOf(result))
    }

    @GetMapping("/most-manga-weekly")
    @ResponseBody
    fun mostMangaWeekly(): ResultEntity {
        val uniqueid = xpRepository.mostMangaReadWeekly()
        var result = catalogRepository.findByUniqueid(uniqueid)
        if (result == null) {
            result = catalogRepository.findMangaRandom(false)
        }
        return ResultEntity(listOf(result))
    }
}