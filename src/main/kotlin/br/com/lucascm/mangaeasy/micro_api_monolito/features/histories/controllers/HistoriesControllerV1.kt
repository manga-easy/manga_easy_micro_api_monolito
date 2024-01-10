package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.controllers
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.repositories.HistoriesRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.VerifyUserIdPermissionService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoriesEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/v1/histories")
class HistoriesControllerV1 {
    @Autowired lateinit var repository: HistoriesRepository
    @Autowired lateinit var handleExceptions: HandleExceptions
    @Autowired lateinit var verifyUserIdPermissionService: VerifyUserIdPermissionService
    @GetMapping
    @ResponseBody
    fun list(
        @RequestParam idUser: String?,
        @RequestParam uniqueId: String?,
        @RequestParam limit: Int?,
        @RequestParam offset: Int?,
        authentication: Authentication
         ): ResultEntity {
        try {
            if (idUser != null) verifyUserIdPermissionService.get(authentication, idUser)
            val result = if (uniqueId != null) {
                repository.findByIduserAndUniqueid(
                    iduser = idUser ?: authentication.principal.toString(),
                    uniqueid = uniqueId
                )
            } else {
                repository.findByIduser(
                    iduser = idUser ?: authentication.principal.toString(),
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

    @PutMapping
    @ResponseBody
    fun update(
        @RequestBody body: HistoriesEntity,
        authentication: Authentication
    ) : ResultEntity {
        try {
            verifyUserIdPermissionService.get(authentication, body.iduser)
            val result = repository.findByIduserAndUniqueid(
              iduser = body.iduser,
              uniqueid = body.uniqueid
            )
            if (result.isEmpty()) {
                repository.save(body.copy(
                    uid = GetUidByFeature().get("users-histories"),
                    createdat = Date().time,
                    updatedat = Date().time,
                ))
            } else {
                val first = result.first()
                repository.save(first.copy(
                    updatedat = body.updatedat,
                    capatual = body.capatual,
                    chapterlidos = body.chapterlidos,
                    currentchapter = body.currentchapter,
                    isdeleted = body.isdeleted,
                    manga = body.manga,
                    uniqueid = body.uniqueid,
                ))
            }
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(body),
                message = "Adicionado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }
}