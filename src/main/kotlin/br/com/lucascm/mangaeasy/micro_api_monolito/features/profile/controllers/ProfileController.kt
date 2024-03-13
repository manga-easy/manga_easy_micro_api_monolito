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
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UserRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

const val LIMIT_FILE_SIZE_GOLD = 3000000
const val LIMIT_FILE_SIZE_IRON = 2000000
const val LIMIT_FILE_SIZE_COPPER = 1000000
const val Membro_Cobre = "61b12f7a0ff25"
const val Membro_Prata = "61b12fddd7201"
const val Membro_Ouro = "61b1302ef0d76"
const val Membro_Platina = "61b130768bd07"
const val Membro_Ferro = "627daca5e13281a2e330"

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
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var bucketRepository: BucketRepository
    @Autowired
    lateinit var usersAchievementsRepository: UsersAchievementsRepository
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
    @PutMapping("/{userID}")
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
            val user = userRepository.search(userID)
            if (user.isEmpty()) throw BusinessException("Usuario não encontrado")
            val result = profileRepository.save(find.copy(
                biography = body.biography,
                updatedAt = Date().time,
                mangasHighlight = body.mangasHighlight,
                achievementsHighlight = body.achievementsHighlight,
                name = user.first().name,
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
    @PutMapping("/{userID}/image")
    fun handleFileUpload(
        @RequestPart file: MultipartFile?,
        @PathVariable userID: String,
        authentication: Authentication
    ): ResultEntity {
        return try {
            var image: String? = null
            verifyUserIdPermissionService.get(authentication, userID)
            val find: ProfileEntity = profileRepository.findByUserID(userID) ?: throw BusinessException("Perfil não encontrado")
            if(file != null){
                validateImage(file, userID)
                bucketRepository.saveImage(userID, file, file.contentType!!)
                image = bucketRepository.getLinkImage(userID)
            }
            val result = profileRepository.save(find.copy(picture = image))
            ResultEntity(
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                total = 1,
                message = "Sucesso"
            )
        } catch (e: Exception){
            handleExceptions.handleCatch(e)
        }
    }
    private fun validateImage(file: MultipartFile, userID: String){
        val limit = getLimitFileByDontion(userID)
        if (file.size > limit) throw BusinessException("Imagem maior que o permetido: ${limit.toString()[0]}mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) throw BusinessException("Tipo de arquivo não permitido.")
        if (typeImage == "GIF" && LIMIT_FILE_SIZE_GOLD != limit) {
            throw BusinessException("Tipo de arquivo permitido apenas para doadores gold e platina.")
        }
    }
    private fun getLimitFileByDontion(userID: String): Int{
        val achievements = usersAchievementsRepository.findAllByUserid(userID)
        var limit = 0
        for (achievement in achievements) {
            if (achievement.idemblema == Membro_Ouro || achievement.idemblema == Membro_Platina) {
                limit = LIMIT_FILE_SIZE_GOLD
                break
            }
            if (achievement.idemblema == Membro_Ferro || achievement.idemblema == Membro_Prata) {
                if (limit < LIMIT_FILE_SIZE_IRON){
                    limit = LIMIT_FILE_SIZE_IRON
                }
            }
            if (achievement.idemblema == Membro_Cobre) {
                if (limit < LIMIT_FILE_SIZE_COPPER){
                    limit = LIMIT_FILE_SIZE_COPPER
                }
            }
        }
        if (limit == 0) {
            throw BusinessException("Para ter Imagem precisa ser pelo menos Doador Bronze")
        }
        return limit
    }
}