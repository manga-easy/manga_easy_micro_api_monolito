package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.controllers

import HistoriesRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/histories")
class HistoriesControllerV1(@Autowired val repository: HistoriesRepository) {
    @Autowired lateinit var handleExceptions: HandleExceptions

    @GetMapping("/list")
    @ResponseBody
    fun list(
            @RequestParam limit: Int,
            @RequestParam offset: Int,
            @RequestParam idUser: String,
            @RequestParam uniqueId: String,
             ): ResultEntity {
        try {
            val result =  repository.findHistoricoWithFilter(idUser,uniqueId,limit,offset)
            return ResultEntity(
                    total =  result.size,
                    status = StatusResultEnum.SUCCESS,
                    data = result,
                    message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }
}