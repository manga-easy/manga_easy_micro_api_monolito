package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.ContentChapterEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.LatestMangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.MandaDetailsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.ContentChapterRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.LatestMangaRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.MangaDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/host-query")
class HostQueryController {
    @Autowired
    lateinit var repository: LatestMangaRepository

    @Autowired
    lateinit var mangaDetailsRepository: MangaDetailsRepository

    @Autowired
    lateinit var contentChapterRepository: ContentChapterRepository

    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @GetMapping("/latest-manga")
    @ResponseBody
    fun getManga(@RequestParam idHost: Int, @RequestParam versionApp: String?)
            : ResultEntity {
        try {
            val result = repository.findById("$idHost<>${versionApp ?: "0.14.0"}")
            if (!result.isPresent) {
                throw BusinessException("Cache não encontrado")
            }
            return ResultEntity(
                message = "Sucesso",
                status = StatusResultEnum.SUCCESS,
                data = listOf(result.get()),
                total = 1
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/latest-manga")
    @ResponseBody
    fun putManga(@RequestBody body: LatestMangaEntity): ResultEntity {
        try {
            if (body.data.isEmpty()) {
                throw BusinessException("Data não pode ser vazio")
            }
            val result = repository.findById(body.getCustom())
            if (!result.isPresent) {
                val resultSave = repository.save(
                    body.copy(
                        id = body.getCustom(),
                        creatAt = Date(),
                        time = 12,
                    )
                )
                return ResultEntity(listOf(resultSave))
            }
            return ResultEntity(listOf(result))
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @GetMapping("/manga-details")
    @ResponseBody
    fun getMangaDetails(
        @RequestParam idHost: Int,
        @RequestParam uniqueid: String,
        @RequestParam versionApp: String?
    )
            : ResultEntity {
        try {
            val result = mangaDetailsRepository.findById("$idHost<>$uniqueid<>${versionApp ?: "0.14.0"}")
            if (!result.isPresent) {
                throw BusinessException("Cache não encontrado")
            }
            return ResultEntity(
                message = "Sucesso",
                status = StatusResultEnum.SUCCESS,
                data = listOf(result.get()),
                total = 1
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/manga-details")
    @ResponseBody
    fun updateMangaDetails(@RequestBody body: MandaDetailsEntity)
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
            val result = mangaDetailsRepository.findById(body.getCustom())
            if (!result.isPresent) {
                val resultSave = mangaDetailsRepository.save(
                    body.copy(
                        id = body.getCustom(),
                        creatAt = Date(),
                        time = 1
                    )
                )
                return ResultEntity(listOf(resultSave))
            }
            ResultEntity(listOf(result))
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
        @RequestParam versionApp: String?
    )
            : ResultEntity {
        try {
            val result = contentChapterRepository.findById("$idHost<>$uniqueid<>$chapter<>${versionApp ?: "0.14.0"}")
            if (!result.isPresent) {
                throw BusinessException("Cache não encontrado")
            }
            return ResultEntity(
                message = "Sucesso",
                status = StatusResultEnum.SUCCESS,
                data = listOf(result.get()),
                total = 1
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/content-chapter")
    @ResponseBody
    fun updateContentChapter(@RequestBody body: ContentChapterEntity)
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
            val result = contentChapterRepository.findById(body.getCustom())
            if (!result.isPresent) {
                val resultSave = contentChapterRepository.save(
                    body.copy(
                        id = body.getCustom(),
                        creatAt = Date(),
                        versionApp = body.versionApp,
                        time = 30,
                    )
                )
                ResultEntity(listOf(resultSave))
            }
            ResultEntity(listOf(result))
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }
}