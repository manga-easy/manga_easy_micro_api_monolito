package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.controllers
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.repositories.HistoriesRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.VerifyUserIdPermissionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/histories")
class HistoriesControllerV1() {
    @Autowired lateinit var repository: HistoriesRepository
    @Autowired lateinit var handleExceptions: HandleExceptions
    @Autowired lateinit var verifyUserIdPermissionService: VerifyUserIdPermissionService

    @GetMapping
    @ResponseBody
    fun list(
        @RequestParam idUser: String,
        @RequestParam uniqueId: String?,
        @RequestParam limit: Int?,
        @RequestParam offset: Int?,
        authentication: Authentication
         ): ResultEntity {
        try {
            verifyUserIdPermissionService.get(authentication, idUser);
            val result = if (uniqueId != null) {
                repository.findByIduserAndUniqueid(iduser = idUser, uniqueid = uniqueId)
            } else {
                repository.findByIduser(
                    iduser = idUser,
                    pageable = PageRequest.of(offset ?: 0, limit ?: 25)
                )
            }
            return ResultEntity(
                total =  result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }
}