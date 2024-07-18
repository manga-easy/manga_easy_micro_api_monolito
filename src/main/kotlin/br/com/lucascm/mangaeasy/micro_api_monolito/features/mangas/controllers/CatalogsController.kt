package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.services.CatalogService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/catalogs")
@Tag(name = "Catalogs")
class CatalogsController {
    @Autowired
    lateinit var catalogService: CatalogService

    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @GetMapping("/v1")
    fun search(
        @RequestParam genres: String?,
        @RequestParam search: String?,
        @RequestParam author: String?,
        @RequestParam page: Int?,
        @RequestParam limit: Int?,
        @RequestParam yearAt: Int?,
        @RequestParam yearFrom: Int?,
        @RequestParam scans: String?,
        @RequestParam isAdult: Boolean?,
    ): List<CatalogEntity> {
        return catalogService.list(
            genres = genres?.split("<>") ?: listOf(),
            search = search,
            author = author,
            isAdult = isAdult ?: false,
            limit = limit,
            page = page,
            yearAt = yearAt,
            yearFrom = yearFrom,
            scans = scans,
        )
    }

    @GetMapping("/v1/{id}")
    fun getById(@PathVariable id: String): CatalogEntity {
        return catalogRepository.findById(id).getOrNull()
            ?: throw BusinessException("Host não encontrado")
    }

    @GetMapping("/v1/over-18")
    fun over18(): List<String> {
        val result = catalogService.list(
            genres = listOf("adult"),
            isAdult = true,
            limit = 20000,
        )
        return result.map { it.uniqueid }.toList()
    }

    @GetMapping("/v1/random")
    fun random(@RequestParam isAdult: Boolean?): CatalogEntity {
        return catalogRepository.findMangaRandom(isAdult ?: false)
    }

    @GetMapping("/v1/most-manga-weekly")
    fun mostMangaWeekly(): CatalogEntity {
        return catalogService.mostMangaWeekly()
    }
}