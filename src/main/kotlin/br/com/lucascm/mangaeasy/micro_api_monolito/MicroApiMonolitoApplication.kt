package br.com.lucascm.mangaeasy.micro_api_monolito

import com.github.sonus21.rqueue.spring.EnableRqueue
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling
import javax.annotation.PostConstruct


@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableRqueue
class MicroApiMonolitoApplication {
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    fun init() {
        try {
            // Inicialização do FirebaseApp
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()
            FirebaseApp.initializeApp(options)
        } catch (e: Exception) {
            log.error("Exception", e)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<MicroApiMonolitoApplication>(*args)
}