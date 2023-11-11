package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UserEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UserRepository
import io.appwrite.Client
import io.appwrite.extensions.toJson
import io.appwrite.models.User
import io.appwrite.services.Users
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.function.ServerResponse.async

@RestController
@RequestMapping("/v1/users")
class UserController {
    @Autowired lateinit var repository: UserRepository

    @GetMapping()
    fun getUsers(@RequestParam search: String?) : ResultEntity {
        try {
            val result = repository.search(search)
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return HandleExceptions().handleCatch(e)
        }
    }

    @GetMapping("/{userid}")
    fun get(@PathVariable userid: String) : ResultEntity {
        try {
            val result = repository.search(userid)
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return HandleExceptions().handleCatch(e)
        }
    }

}