package br.com.lucascm.mangaeasy.micro_api_monolito.core.configs

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import io.sentry.Sentry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler
    : ResponseEntityExceptionHandler() {
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessError(e: BusinessException): ResponseEntity<Any> {
        return ResponseEntity<Any>(
            mapOf("message" to e.message, "code" to e.code),
            HttpStatus.UNPROCESSABLE_ENTITY
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleInternalServerError(e: Exception): ResponseEntity<Any> {
        Sentry.captureException(e)
        log.error("Exception", e)
        return ResponseEntity<Any>(
            mapOf("message" to "Ocorreu um erro no serviço"),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}