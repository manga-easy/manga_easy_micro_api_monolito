package br.com.lucascm.mangaeasy.micro_api_monolito.features.chapter_notifications.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.features.chapter_notifications.entities.ChapterNotificationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.chapter_notifications.repositories.ChapterNotificationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*



@RestController
@RequestMapping("/v1/chapter-notifications")
class ChapterNotificationsController(@Autowired val repository: ChapterNotificationsRepository) {
    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam uniqueid : String,
             @RequestParam idhost : Int,
             @RequestParam datetime : Long
    ) : ResultEntity {
        try {
            val result: List<ChapterNotificationsEntity> = repository.findAllByUniqueidAndIdhostAndDatetime(
                uniqueid, idhost, datetime
            )
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return ResultEntity(
                total = 0,
                status = StatusResultEnum.ERROR,
                data = listOf(),
                message = e.message
            )
        }
    }
}