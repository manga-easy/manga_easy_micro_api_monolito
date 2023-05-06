package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import mu.KotlinLogging
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val jwtDecoder: JwtDecoder,
    private val logger: KotlinLogging = KotlinLogging
) {

    fun parseToken(token: String?): String? {
        return try {
            val jwt = jwtDecoder.decode(token)
           return  jwt.claims["userId"] as String?
        } catch (e: Exception) {
            logger.logger("TokenService").info(e.message)
            null
        }
    }
}