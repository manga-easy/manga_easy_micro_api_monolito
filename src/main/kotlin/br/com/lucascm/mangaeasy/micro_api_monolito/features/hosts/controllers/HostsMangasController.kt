package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos.HostMangaChapterDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos.HostMangaDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos.HostMangaLastUpdatedDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos.HostMangaSearchDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.DetailsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.ImageChapterEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.MangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.ContentChapterRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.HostMangaSearchRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.LatestMangaRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.MangaDetailsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/hosts/{hostId}")
@Tag(name = "Hosts")
class HostsMangasController {
    companion object {
        const val CACHE_NOT_FOUND = "Cache não encontrado"
    }

    @Autowired
    lateinit var repository: LatestMangaRepository

    @Autowired
    lateinit var hostMangaSearchRepository: HostMangaSearchRepository

    @Autowired
    lateinit var mangaDetailsRepository: MangaDetailsRepository

    @Autowired
    lateinit var contentChapterRepository: ContentChapterRepository

    @Autowired
    lateinit var toggleService: ToggleService

    @GetMapping("/mangas/v1/last-updated")
    fun getLastUpdated(@PathVariable hostId: Int): List<MangaEntity> {
        val result = repository.findById("$hostId").getOrNull()
            ?: throw BusinessException(CACHE_NOT_FOUND)
        return result.data
    }

    @PutMapping("/mangas/v1/last-updated")
    fun saveLastUpdated(
        @PathVariable hostId: Int,
        @RequestBody body: HostMangaLastUpdatedDto
    ): List<MangaEntity> {
        if (body.data.isEmpty()) {
            throw BusinessException("Data não pode ser vazio")
        }
        handleVersionApp(body.versionApp)
        return repository.save(body.toEntity(hostId)).data
    }

    @GetMapping("/mangas/v1/{uniqueId}")
    fun getManga(
        @PathVariable hostId: Int,
        @PathVariable uniqueId: String,
    ): DetailsEntity {
        val result = mangaDetailsRepository.findById("$hostId<>$uniqueId")
        if (!result.isPresent) {
            throw BusinessException(CACHE_NOT_FOUND)
        }
        return result.get().data
    }

    @PutMapping("/mangas/v1/{uniqueId}")
    fun updateManga(
        @PathVariable hostId: Int,
        @PathVariable uniqueId: String,
        @RequestBody body: HostMangaDto
    ): DetailsEntity {
        if (body.data.title.isEmpty()) {
            throw BusinessException("Titulo do mangá não pode ser vazio")
        }
        if (body.data.capitulos.isEmpty()) {
            throw BusinessException("Capitulos do mangá não pode ser vazio")
        }
        if (body.data.capa.isEmpty()) {
            throw BusinessException("Capa do mangá não pode ser vazio")
        }
        handleVersionApp(body.versionApp)

        return mangaDetailsRepository.save(
            body.toEntity(
                hostId = hostId,
                uniqueId = uniqueId
            )
        ).data
    }

    @GetMapping("/mangas/v1/{uniqueId}/chapters/{chapterId}")
    fun getChapter(
        @PathVariable hostId: Int,
        @PathVariable uniqueId: String,
        @PathVariable chapterId: String,
    ): List<ImageChapterEntity> {
        val result = contentChapterRepository.findById("$hostId<>$uniqueId<>$chapterId")
        if (!result.isPresent) {
            throw BusinessException(CACHE_NOT_FOUND)
        }
        return result.get().data
    }

    @PutMapping("/mangas/v1/{uniqueId}/chapters/{chapterId}")
    fun saveChapter(
        @PathVariable hostId: Int,
        @PathVariable uniqueId: String,
        @PathVariable chapterId: String,
        @RequestBody body: HostMangaChapterDto
    ): List<ImageChapterEntity> {
        if (body.data.isEmpty()) {
            throw BusinessException("data não pode ser vazio")
        }
        handleVersionApp(body.versionApp)
        return contentChapterRepository.save(
            body.toEntity(
                hostId = hostId,
                uniqueId = uniqueId,
                chapterId = chapterId
            )
        ).data
    }


    @GetMapping("/mangas/v1/search")
    fun getSearch(
        @PathVariable hostId: Int,
        @RequestParam search: String
    ): List<MangaEntity> {
        val result = hostMangaSearchRepository.findById("$hostId<>$search").getOrNull()
            ?: throw BusinessException(CACHE_NOT_FOUND)
        return result.data
    }

    @PutMapping("/mangas/v1/search")
    fun updateSearch(
        @PathVariable hostId: Int,
        @RequestBody body: HostMangaSearchDto
    ): List<MangaEntity> {
        if (body.data.isEmpty()) {
            throw BusinessException("data não pode ser vazio")
        }
        if (body.search.isEmpty()) {
            throw BusinessException("search não pode ser vazio")
        }
        handleVersionApp(body.versionApp)
        val result = hostMangaSearchRepository.save(
            body.toEntity(hostId = hostId)
        )
        return result.data
    }

    private fun handleVersionApp(versionApp: String) {
        val toggle = toggleService.getToggle(ToggleEnum.currentVersionApp)
        //Não deixar atualizar o cache versões desatualizadas ou beta
        if (toggle != versionApp) throw BusinessException("Somente a versão atual pode atualizar o cache")
    }
}