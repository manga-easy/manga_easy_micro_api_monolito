package br.com.lucascm.mangaeasy.micro_api_monolito.core.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class VerifyUserIdPermissionService(private val getIsUserAdminService: GetIsUserAdminService) {
   fun get(authentication: Authentication, userIdUrl: String){
       val userIdToken = authentication.principal.toString()
       if (getIsUserAdminService.get(userIdToken)){
           return;
       }
       if (userIdUrl == userIdToken){
           return;
       }
       throw Exception("Usuario incorreto ao Token")
   }
}