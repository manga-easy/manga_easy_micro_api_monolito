package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.repositories.PermissionsRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HandlerUserAdmin(@Autowired val repository: PermissionsRepository, val logger: KotlinLogging = KotlinLogging) {
    @Deprecated("User handleIsAdmin")
    fun get(userId: String) : Boolean{
        try {
            val result = repository.findByUserid(userId)
            return result?.value  == 90//significa que é um admin
        }catch (e: Exception){
            KotlinLogging.logger("GetIsUserAdminService").catching(e)
            return  false
        }
    }

    fun handleIsAdmin(userId: String): Unit {
        val result = repository.findByUserid(userId)
        ///significa que é um admin
        if (result?.value != 90){
            throw BusinessException("O usuario não tem permissão")
        }
        return
    }
}