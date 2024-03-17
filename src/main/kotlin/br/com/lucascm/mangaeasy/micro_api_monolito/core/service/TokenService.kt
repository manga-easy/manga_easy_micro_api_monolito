package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.stereotype.Service

@Service
class TokenService(private val jwtDecoder: JwtDecoder) {

    fun parseToken(token: String?): String? {
        return try {
            if (token == null) return null
            if (token.isEmpty()) return null
            val jwt = jwtDecoder.decode(token)
           return  jwt.claims["userId"] as String?
        } catch (e: Exception) {
            null
        }
    }
}