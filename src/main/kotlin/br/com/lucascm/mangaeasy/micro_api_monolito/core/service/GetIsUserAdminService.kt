package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.repositories.PermissionsRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetIsUserAdminService(@Autowired val repository: PermissionsRepository, val logger: KotlinLogging = KotlinLogging) {
    fun get(userId: String) : Boolean{
        logger.logger("GetIsUserAdmin").info(userId)
        val result = repository.findByUserid(userId)

        return result?.value  == 90
    }
}