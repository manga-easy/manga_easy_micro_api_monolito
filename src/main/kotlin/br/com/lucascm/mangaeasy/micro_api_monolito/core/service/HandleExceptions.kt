package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import mu.KotlinLogging

class HandleExceptions<T> {
    fun handleCatch(e: Exception): ResultEntity<T>{
        var message = "Ocorreu um erro no serviço"
        if (e is BusinessException){
            message = e.message
        }else{
            KotlinLogging.logger("permissions").catching(e)
        }
        return ResultEntity(
            total = 0,
            status = StatusResultEnum.ERROR,
            data = listOf(),
            message = message
        )
    }
}