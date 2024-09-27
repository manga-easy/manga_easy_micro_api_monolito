package br.com.lucascm.mangaeasy.micro_api_monolito.core.configs

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.swiftzer.semver.SemVer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class AppVersionFilter : Filter {
    @Autowired
    lateinit var toggleService: ToggleService
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        val uri = httpRequest.requestURI
        val headerVersion = httpRequest.getHeader("me-app-version")
        val versionApp = SemVer.parse(headerVersion ?: "0.15.4")

        // Ignora as URLs /doc/** e /rqueue/**
        if (!uri.startsWith("/doc/") && !uri.startsWith("/rqueue/")) {
            val minimumVersionApp = SemVer.parse(
                toggleService.getToggle(ToggleEnum.minimalVersionApp)
            )
            // Verificar se a versão do aplicativo é muito antiga
            if (versionApp < minimumVersionApp) {
                val objectMapper = ObjectMapper()
                val errorMessage =
                    mapOf("message" to "A versão do aplicativo é muito antiga. Por favor, atualize para continuar.")
                val jsonError = objectMapper.writeValueAsString(errorMessage)
                httpResponse.contentType = "application/json"
                httpResponse.status = 422
                httpResponse.writer.write(jsonError)
                return
            }
        }
        // Continuar com o fluxo normal da requisição
        chain.doFilter(request, response)
    }
}