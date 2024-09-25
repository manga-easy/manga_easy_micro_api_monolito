package br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.RedisCacheName
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.env.Environment
import org.springframework.core.env.StandardEnvironment
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ToggleService {
    val env: Environment = StandardEnvironment()
    val client = RestTemplate()
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Cacheable(value = [RedisCacheName.TOGGLE])
    fun getToggle(toggle: ToggleEnum): String {
        return try {
            val url = "${env.getProperty("toggle.url")!!}/v1/toggle/search?name=${toggle.name}"
            val result = client.getForEntity(
                url,
                ResponseData::class.java
            )
            result.body!!.data.first().value.toString()
        } catch (e: Exception) {
            log.error("Exception", e)
            return toggle.default.toString()
        }
    }
}