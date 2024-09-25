package br.com.lucascm.mangaeasy.micro_api_monolito.core.configs

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.TokenService
import org.springframework.beans.factory.annotation.Autowired
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
import org.springframework.web.reactive.config.ResourceHandlerRegistry

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenService: TokenService,
) {
    @Autowired
    lateinit var appVersionFilter: AppVersionFilter

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // Define public and private routes
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET, "/v1/recommendations/list").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/banners/list").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/hosts/list").permitAll()
            .requestMatchers(HttpMethod.GET, "/banners/v1").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/banners/list").permitAll()
            .requestMatchers(HttpMethod.GET, "/hosts/v1").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/notifications/list").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/catalog/over-18").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/catalog/most-manga-weekly").permitAll()
            .requestMatchers(HttpMethod.GET, "/doc/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/rqueue/**").permitAll()
            .requestMatchers("**").authenticated()

        // Configure JWT
        http.oauth2ResourceServer().jwt()
        http.authenticationManager { auth ->
            val jwt = auth as BearerTokenAuthenticationToken
            val userId = tokenService.parseToken(jwt.token) ?: throw InvalidBearerTokenException("Invalid token")
            UsernamePasswordAuthenticationToken(userId, "", listOf(SimpleGrantedAuthority("USER")))
        }

        // Other configuration
        http.cors().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.csrf().disable()
        http.headers().frameOptions().disable()
        http.headers().xssProtection().disable()

        return http.build()
    }

    @Bean
    fun appVersionFilterRegistration(): FilterRegistrationBean<AppVersionFilter> {
        val registration = FilterRegistrationBean(appVersionFilter)
        registration.addUrlPatterns("/*")
        return registration
    }

    fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations("classpath:/public/")
        }
    }
}