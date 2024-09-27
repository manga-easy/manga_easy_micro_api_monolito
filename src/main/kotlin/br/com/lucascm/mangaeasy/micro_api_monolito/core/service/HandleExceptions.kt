package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import io.sentry.Sentry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Deprecated("Descontinuado")
class HandleExceptions {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    fun handleCatch(e: Exception): ResultEntity {
        if (e is BusinessException) {
            return ResultEntity(
                total = 0,
                status = StatusResultEnum.ERROR,
                data = listOf(),
                message = e.message
            )
        }
        Sentry.captureException(e);
        log.error("HandleExceptions", e)
        throw e
    }
}