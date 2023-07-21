package br.com.lucascm.mangaeasy.micro_api_monolito

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.annotation.PostConstruct

@SpringBootApplication
class MicroApiMonolitoApplication {
	@PostConstruct
	fun init() {
		// Inicialização do FirebaseApp
		val options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.getApplicationDefault())
			.build()
		FirebaseApp.initializeApp(options)
	}
}
fun main(args: Array<String>) {
	runApplication<MicroApiMonolitoApplication>(*args)
}
