package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class VerifyUserIdPermissionService(private val handlerUserAdmin: HandlerUserAdmin) {
   fun get(authentication: Authentication, userIdUrl: String){
       val userIdToken = authentication.principal.toString()
       if (userIdUrl == userIdToken){
           return
       }
       handlerUserAdmin.handleIsAdmin(userIdToken)
   }
}