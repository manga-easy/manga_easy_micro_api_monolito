package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UserEntity
import io.appwrite.Client
import io.appwrite.extensions.toJson
import io.appwrite.models.User
import io.appwrite.services.Users
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Repository

@Repository
class UserRepository {
    @Autowired
    lateinit var env: Environment
    fun serachUser(search: String?): List<UserEntity>{
        val response = runBlocking { futureUsers(search) }
        return response.map { e -> UserEntity(
            email = e.email,
            emailverification = e.emailVerification,
            registration = e.registration,
            prefs = e.prefs.toJson(),
            uid = e.id,
            name = e.name,
            status = e.status
        ) }.toList()
    }

    private suspend fun futureUsers(search: String?) : List<User<Any>>{
        val client = Client(endPoint = "https://${env.getProperty("auth.endpoint")!!}/v1", selfSigned = true)
            .setProject(env.getProperty("auth.idproject")!!) // Your project ID
            .setKey(env.getProperty("auth.key")!!) // Your secret API key

        val users = Users(client)
        return users.list(search = search).users
    }
}