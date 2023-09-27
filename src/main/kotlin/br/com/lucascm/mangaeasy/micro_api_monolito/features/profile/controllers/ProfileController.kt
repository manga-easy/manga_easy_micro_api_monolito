package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.VerifyUserIdPermissionService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.repositories.SeasonsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersLevelsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersLevelsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/v1/profile")
class ProfileController {
    @Autowired
    lateinit var verifyUserIdPermissionService: VerifyUserIdPermissionService
    @Autowired
    lateinit var profileRepository: ProfileRepository
    @Autowired
    lateinit var handleExceptions: HandleExceptions
    @Autowired
    lateinit var usersLevelsRepository: UsersLevelsRepository
    @Autowired
    lateinit var seasonsRepository: SeasonsRepository
    @GetMapping("/{userID}")
    @ResponseBody
    fun getProfile(authentication: Authentication, @PathVariable userID: String): ResultEntity {
        return try {
            verifyUserIdPermissionService.get(authentication, userID)
            var result = profileRepository.findByUserID(userID)
            if (result == null){
                result = ProfileEntity(
                    uid = GetUidByFeature().get("profile"),
                    updatedAt = Date().time,
                    biography = "",
                    createdAt = Date().time,
                    achievementsHighlight = listOf(),
//                    currentLevel = getCurrentLevel(userID),
                    mangasHighlight = listOf(),
                    userID = userID,
                    totalMangaRead = 0,
                    totalAchievements = 0,
                    role = "Aventureiro"
                )
                profileRepository.save(result)
            }
            ResultEntity(
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                total = 1,
                message = "Sucesso"
            )
        }catch (e: Exception){
            handleExceptions.handleCatch(e)
        }
    }
    @PostMapping("/{userID}")
    @ResponseBody
    fun updateProfile(authentication: Authentication,
                      @RequestBody body: ProfileEntity,
                      @PathVariable userID: String
    ): ResultEntity {
        return try {
            verifyUserIdPermissionService.get(authentication, userID)
            val find = profileRepository.findByUserID(userID)
            if (find == null){
               throw BusinessException("Perfil não encontrado")
            }
            val result = profileRepository.save(find.copy(
                biography = body.biography,
//                currentLevel = getCurrentLevel(userID),
                updatedAt = Date().time,
                mangasHighlight = body.mangasHighlight,
                achievementsHighlight = body.achievementsHighlight,
            ))
            ResultEntity(
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                total = 1,
                message = "Sucesso"
            )
        }catch (e: Exception){
            handleExceptions.handleCatch(e)
        }
    }

    private fun getCurrentLevel(userID: String): UsersLevelsEntity {
        val season = seasonsRepository.findTop1ByOrderByNumberDesc().uid!!
        val result = usersLevelsRepository.findByTemporadaAndUserid(season, userID)
        return result.first()
    }
}