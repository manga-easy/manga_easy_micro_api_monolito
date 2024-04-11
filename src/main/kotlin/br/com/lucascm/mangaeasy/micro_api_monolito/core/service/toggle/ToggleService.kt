package br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ToggleService {
    @Autowired
    lateinit var env: Environment
    val client = RestTemplate()
    fun <T> getToggle(toggle: ToggleEnum): T {
        val url = "${env.getProperty("toggle.url")!!}/v1/toggle/search?name=$toggle"
        val result = client.getForEntity(
            url,
            ResponseData::class.java
        )
        return result.body!!.data.first().value as T
    }
}