package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import io.sentry.Sentry
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.Objects

@Service
class HandleExceptions {
    fun handleCatch(e: Exception): ResultEntity{
        var message = "Ocorreu um erro no serviço"
        if (e is BusinessException){
            message = e.message
        }else{
            Sentry.captureException(e);
            KotlinLogging.logger("HandleExceptions").catching(e)
        }
        return ResultEntity(
            total = 0,
            status = StatusResultEnum.ERROR,
            data = listOf(),
            message = message
        )
    }
}