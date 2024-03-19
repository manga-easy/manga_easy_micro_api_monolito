package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.VerifyUserIdPermissionService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.earnXpDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.XpRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.random.Random

@RestController
@RequestMapping("/v1/level")
class LevelController {
    @Autowired
    lateinit var verifyUserIdPermissionService: VerifyUserIdPermissionService
    @Autowired
    lateinit var xpRepository: XpRepository
    @Autowired
    lateinit var catalogRepository: CatalogRepository
    @GetMapping("/{userID}")
    @ResponseBody
    fun getXp(authentication: Authentication, @PathVariable userID: String): ResultEntity {
        try {
            verifyUserIdPermissionService.get(authentication, userID)
            val result = xpRepository.countXpTotal(userID)
            return ResultEntity(listOf(result ?: 0))
        }catch (e: Exception){
            return HandleExceptions().handleCatch(e)
        }
    }
    @PutMapping("/{userID}")
    @ResponseBody
    fun earnXp(authentication: Authentication,
               @PathVariable userID: String,
               @RequestBody body: earnXpDto
    ): ResultEntity{
        try {
            verifyUserIdPermissionService.get(authentication, userID)
            val manga = catalogRepository.findByUniqueid(body.uniqueID)
            if (manga == null) throw BusinessException("Manga não catalogado")
            val result = xpRepository.findByUserIDAndUniqueIDAndChapterNumber(
                userID,
                body.uniqueID,
                body.chapterNumber
            )
            if (result == null){
                xpRepository.save(XpEntity(
                    uniqueID = body.uniqueID,
                    chapterNumber = body.chapterNumber,
                    userID = userID,
                    createdAt = Date().time,
                    updatedAt = Date().time,
                    quantity = Random.nextInt(1, 6).toLong(),
                    totalMinutes = 1
                ))
                return ResultEntity(listOf(true))
            }
            if (result.quantity > 50) throw BusinessException("Voçê ja atingiu o maximo de xp para esse capitulo do manga")
            xpRepository.save(result.copy(
                    updatedAt = Date().time,
                    totalMinutes = ++result.totalMinutes,
                    quantity = result.quantity + Random.nextInt(1, 6).toLong()
                )
            )
            return ResultEntity(listOf(false))
        }catch (e: Exception){
            return HandleExceptions().handleCatch(e)
        }
    }
}