package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.ContentChapterRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.LatestMangaRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.MangaDetailsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos.ContentChapterDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos.LatestMangaDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos.MangaDetailsDto
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/host-query")
@Deprecated("Remover 0.18 -> 0.20")
@Tag(name = "Hosts")
class HostQueryController {
    @Autowired
    lateinit var repository: LatestMangaRepository

    @Autowired
    lateinit var mangaDetailsRepository: MangaDetailsRepository

    @Autowired
    lateinit var contentChapterRepository: ContentChapterRepository

    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @Autowired
    lateinit var toggleService: ToggleService

    @GetMapping("/latest-manga")
    @ResponseBody
    fun getManga(@RequestParam idHost: Int)
            : ResultEntity {
        return try {
            val result = repository.findById("$idHost")
            if (!result.isPresent) {
                throw BusinessException("Cache não encontrado")
            }
            ResultEntity(data = listOf(result.get()))
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/latest-manga")
    @ResponseBody
    fun putManga(@RequestBody body: LatestMangaDto): ResultEntity {
        return try {
            if (body.data.isEmpty()) {
                throw BusinessException("Data não pode ser vazio")
            }
            handleVersionApp(body.versionApp)
            val resultSave = repository.save(body.toEntity())
            ResultEntity(listOf(resultSave))
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @GetMapping("/manga-details")
    @ResponseBody
    fun getMangaDetails(
        @RequestParam idHost: Int,
        @RequestParam uniqueid: String,
    )
            : ResultEntity {
        return try {
            val result = mangaDetailsRepository.findById("$idHost<>$uniqueid")
            if (!result.isPresent) {
                throw BusinessException("Cache não encontrado")
            }
            ResultEntity(data = listOf(result.get()))
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/manga-details")
    @ResponseBody
    fun updateMangaDetails(@RequestBody body: MangaDetailsDto)
            : ResultEntity {
        return try {
            if (body.uniqueid.isEmpty()) {
                throw BusinessException("uniqueid não pode ser vazio")
            }
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

            val resultSave = mangaDetailsRepository.save(
                body.toEntity()
            )
            return ResultEntity(listOf(resultSave))

        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @GetMapping("/content-chapter")
    @ResponseBody
    fun getContentChapter(
        @RequestParam idHost: Int,
        @RequestParam uniqueid: String,
        @RequestParam chapter: String,
    )
            : ResultEntity {
        return try {
            val result = contentChapterRepository.findById("$idHost<>$uniqueid<>$chapter")
            if (!result.isPresent) {
                throw BusinessException("Cache não encontrado")
            }
            ResultEntity(
                data = listOf(result.get())
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/content-chapter")
    @ResponseBody
    fun updateContentChapter(@RequestBody body: ContentChapterDto)
            : ResultEntity {
        return try {
            if (body.uniqueid.isEmpty()) {
                throw BusinessException("uniqueid não pode ser vazio")
            }
            if (body.data.isEmpty()) {
                throw BusinessException("data não pode ser vazio")
            }
            if (body.chapter.isEmpty()) {
                throw BusinessException("chapter não pode ser vazio")
            }
            handleVersionApp(body.versionApp)

            val resultSave = contentChapterRepository.save(
                body.toEntity()
            )
            ResultEntity(listOf(resultSave))

        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    private fun handleVersionApp(versionApp: String) {
        val toggle = toggleService.getToggle(ToggleEnum.currentVersionApp)
        //Não deixar atualizar o cache versões desatualizadas ou beta
        if (toggle != versionApp) throw BusinessException("Somente a versão atual pode atualizar o cache")
    }
}