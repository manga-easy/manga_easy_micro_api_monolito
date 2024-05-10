package br.com.lucascm.mangaeasy.micro_api_monolito

import br.com.lucascm.mangaeasy.micro_api_monolito.core.configs.RedisRepository
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableRedisRepositories(repositoryFactoryBeanClass = RedisRepository::class)
class MicroApiMonolitoApplication {
    @PostConstruct
    fun init() {
        try {
            // Inicialização do FirebaseApp
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()
            FirebaseApp.initializeApp(options)
        } catch (e: Exception) {
            KotlinLogging.logger("MicroApiMonolitoApplication").catching(e)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<MicroApiMonolitoApplication>(*args)
}