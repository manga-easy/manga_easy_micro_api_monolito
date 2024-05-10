package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import io.sentry.Sentry
import mu.KotlinLogging
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class HandleResponseApi {
    fun error(e: Exception): ResponseEntity<Any> {
        var message = "Ocorreu um erro no serviço"
        if (e is BusinessException) {
            message = e.message
            return ResponseEntity<Any>(message, HttpStatusCode.valueOf(422))
        }
        Sentry.captureException(e);
        KotlinLogging.logger("HandleResponseApi").catching(e)

        return ResponseEntity<Any>(message, HttpStatusCode.valueOf(500))
    }

    fun ok(body: Any?): ResponseEntity<Any> {
        return ResponseEntity<Any>(body, HttpStatusCode.valueOf(200))
    }
}