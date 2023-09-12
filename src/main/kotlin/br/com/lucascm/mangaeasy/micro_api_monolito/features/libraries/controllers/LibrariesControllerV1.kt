package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.controllers
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.VerifyUserIdPermissionService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.LibrariesEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.repositories.LibrariesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v2/libraries")
class LibrariesControllerV1  {
    @Autowired lateinit var repository: LibrariesRepository
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
            verifyUserIdPermissionService.get(authentication, idUser)
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

    @PutMapping
    @ResponseBody
    fun update(
        @RequestBody body: LibrariesEntity,
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
                    uid = GetUidByFeature().get("users-libraries"),
                    createdat = Date().time,
                    updatedat = Date().time,
                ))
            } else {
                val first = result.first()
                repository.save(first.copy(updatedat = Date().time,
                    status  = body.status,
                    isdeleted = body.isdeleted,
                    issync = body.issync,
                    manga = body.manga
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