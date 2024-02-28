package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.VerifyUserIdPermissionService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.BucketRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.repositories.SeasonsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersLevelsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersLevelsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

const val LIMIT_FILE_SIZE = 3000000
val TYPE_CONTENT_IMAGE = listOf("JPG", "GIF", "PNG", "JPEG")

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
    lateinit var bucketRepository: BucketRepository
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
                    role = "Aventureiro",
                    picture = null
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
                //currentLevel = getCurrentLevel(userID),
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
    @PutMapping("/{userID}/image")
    fun handleFileUpload(
        @RequestPart file: MultipartFile,
        @PathVariable userID: String
    ): ResultEntity {
        return try {
//            var find: ProfileEntity = profileRepository.findByUserID(userID) ?: throw BusinessException("Perfil não encontrado")
            validateImage(file)
            bucketRepository.saveImage(userID, file, file.contentType!!)
            val image = bucketRepository.getLinkImage(userID)
//            val result = profileRepository.save(find.copy(picture = image))
            ResultEntity(
                status = StatusResultEnum.SUCCESS,
                data = listOf(),
                total = 1,
                message = "Sucesso"
            )
        } catch (e: Exception){
            handleExceptions.handleCatch(e)
        }
    }
    private fun validateImage(file: MultipartFile){
        if (file.size > LIMIT_FILE_SIZE) throw BusinessException("Imagem maior que o permetido: 3mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) throw BusinessException("Tipo de arquivo não permitido.")
    }
}