package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.repositories.PermissionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class HandlerPermissionUser {
    @Autowired
    lateinit var repository: PermissionsRepository
    fun handleIsAdmin(userAuth: UserAuth) {
        val result = repository.findByUserId(userAuth.userId)
        ///significa que é um admin
        if (result.getOrNull()?.level != 90) {
            throw BusinessException("O usuario não tem permissão")
        }
    }

    //Verificar se o usuario do token é o mesmo da url, caso for admin ele faz oque quiser
    fun handleIsOwnerToken(userAuth: UserAuth, userIdUrl: String) {
        if (userIdUrl == userAuth.userId) {
            return
        }
        handleIsAdmin(userAuth)
    }
}