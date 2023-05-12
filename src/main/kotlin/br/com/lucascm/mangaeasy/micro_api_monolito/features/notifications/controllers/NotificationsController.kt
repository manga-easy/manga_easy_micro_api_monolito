package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.repositories.NotificationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/notifications")
class NotificationsController(@Autowired val repository: NotificationsRepository) {
    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam status : String?,
             @RequestParam idhost : Int?
    ) : ResultEntity<NotificationsEntity> {
        try {
            val result: List<NotificationsEntity> = repository.findTop25ByOrderByCreatedatDesc()
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