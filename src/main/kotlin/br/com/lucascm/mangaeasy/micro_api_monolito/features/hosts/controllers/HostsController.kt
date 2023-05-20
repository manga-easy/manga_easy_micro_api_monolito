package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.HostsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*



@RestController
@RequestMapping("/v1/hosts")
class HostsController(@Autowired val repository: HostsRepository) {
    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam status : String?,
             @RequestParam idhost : Int?
    ) : ResultEntity<HostsEntity> {
        try {
            val result: List<HostsEntity> = if (idhost == null) {
                repository.findAllByStatus(status ?: "enable")
            }else{
                repository.findAllByIdhost(idhost)
            }
            return ResultEntity<HostsEntity>(
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