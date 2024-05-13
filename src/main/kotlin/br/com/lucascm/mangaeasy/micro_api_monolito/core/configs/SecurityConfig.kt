package br.com.lucascm.mangaeasy.micro_api_monolito.core.configs

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.TokenService
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
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenService: TokenService,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // Define public and private routes
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET, "/v1/recommendations/list").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/banners/list").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/hosts/list").permitAll()
            .requestMatchers(HttpMethod.GET, "/v2/banners/list").permitAll()
            .requestMatchers(HttpMethod.GET, "/v2/hosts/list").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/seasons/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/notifications/list").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/catalog/over-18").permitAll()
            .requestMatchers("**").authenticated()

        // Configure JWT
        http.oauth2ResourceServer().jwt()
        http.authenticationManager { auth ->
            val jwt = auth as BearerTokenAuthenticationToken
            val userId = tokenService.parseToken(jwt.token) ?: throw InvalidBearerTokenException("Invalid token")
            UsernamePasswordAuthenticationToken(userId, "", listOf(SimpleGrantedAuthority("USER")))
        }

        // Other configuration
        http.cors()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.csrf().disable()
        http.headers().frameOptions().disable()
        http.headers().xssProtection().disable()

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        // allow localhost for dev purposes
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:8080", "https://dash.mangaeasy.online")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("authorization", "content-type")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun appVersionFilterRegistration(): FilterRegistrationBean<AppVersionFilter> {
        val registration = FilterRegistrationBean(AppVersionFilter())
        registration.addUrlPatterns("/*")
        return registration
    }
}

@Component
class AppVersionFilter : Filter {
    val toggleService: ToggleService = ToggleService()
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        val headerVersion = httpRequest.getHeader("me-app-version")
        val versionApp = SemVer.parse(headerVersion ?: "0.15.4")
        val minimumVersionApp = SemVer.parse(toggleService.getToggle(ToggleEnum.minimalVersionApp))

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
        // Continuar com o fluxo normal da requisição
        chain.doFilter(request, response)
    }
}