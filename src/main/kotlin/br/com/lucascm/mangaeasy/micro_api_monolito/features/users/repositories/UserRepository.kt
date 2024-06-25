package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UserEntity
import io.appwrite.Client
import io.appwrite.extensions.toJson
import io.appwrite.services.Users
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    @Autowired
    var env: Environment
) {


    private val client: Client

    init {
        client = Client(endPoint = "http://${env.getProperty("auth.endpoint")!!}/v1", selfSigned = true)
            .setProject(env.getProperty("auth.idproject")!!) // Your project ID
            .setKey(env.getProperty("auth.key")!!) // Your secret API key
    }

    fun search(search: String?): List<UserEntity> {
        val response = runBlocking { Users(client).list(search = search).users }
        return response.map { e ->
            UserEntity(
                email = e.email,
                emailverification = e.emailVerification,
                registration = e.registration,
                prefs = e.prefs.toJson(),
                uid = e.id,
                name = e.name,
                status = e.status
            )
        }.toList()
    }

    fun getId(id: String): UserEntity {
        val e = runBlocking { Users(client).get(id) }
        return UserEntity(
            email = e.email,
            emailverification = e.emailVerification,
            registration = e.registration,
            prefs = e.prefs.toJson(),
            uid = e.id,
            name = e.name,
            status = e.status
        )
    }


}