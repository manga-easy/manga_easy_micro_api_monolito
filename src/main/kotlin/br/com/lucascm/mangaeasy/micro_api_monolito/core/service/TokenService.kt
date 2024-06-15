package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.stereotype.Service

@Service
class TokenService(private val jwtDecoder: JwtDecoder) {
    fun parseToken(token: String?): UserAuth? {
        return try {
            if (token == null) return null
            if (token.isEmpty()) return null
            val jwt = jwtDecoder.decode(token)
            return UserAuth(jwt.claims["userId"] as String)
        } catch (e: Exception) {
            null
        }
    }
}