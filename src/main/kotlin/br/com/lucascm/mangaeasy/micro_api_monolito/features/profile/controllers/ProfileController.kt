package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.VerifyUserIdPermissionService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.repositories.LibrariesRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.FavoriteAchievement
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.FavoriteManga
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.BucketProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UserRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
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
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var bucketProfileRepository: BucketProfileRepository

    @Autowired
    lateinit var usersAchievementsRepository: UsersAchievementsRepository

    @Autowired
    lateinit var librariesRepository: LibrariesRepository

    @GetMapping("/{userID}")
    @ResponseBody
    fun getProfile(authentication: Authentication, @PathVariable userID: String): ResultEntity {
        return try {
            val user = userRepository.search(userID)
            if (user.isEmpty()) throw BusinessException("Usuario não encontrado")
            var result = profileRepository.findByUserID(userID)
            if (result == null) {
                val totalMangaRead = librariesRepository.countByStatusAndUserId(userID)
                val totalAchievements = usersAchievementsRepository.countByUserId(userID)
                result = ProfileEntity(
                    updatedAt = Date().time,
                    biography = "",
                    createdAt = Date.from(Instant.parse(user.first().registration)).time,
                    achievementsHighlight = listOf(),
                    mangasHighlight = listOf(),
                    userID = userID,
                    totalMangaRead = totalMangaRead,
                    totalAchievements = totalAchievements,
                    role = "Aventureiro",
                    name = user.first().name,
                    totalXp = 0,
                )
                profileRepository.save(result)
            }
            ResultEntity(
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                total = 1,
                message = "Sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/{userID}")
    @ResponseBody
    fun updateProfile(
        authentication: Authentication,
        @RequestBody body: ProfileEntity,
        @PathVariable userID: String
    ): ResultEntity {
        return try {
            verifyUserIdPermissionService.get(authentication, userID)
            val find = profileRepository.findByUserID(userID) ?: throw BusinessException("Perfil não encontrado")
            val result = profileRepository.save(
                find.copy(
                    biography = body.biography,
                    updatedAt = Date().time,
                    name = body.name,
                    visibleStatics = body.visibleStatics,
                    visibleAchievements = body.visibleAchievements,
                    visibleMangas = body.visibleMangas,
                )
            )
            ResultEntity(
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                total = 1,
                message = "Sucesso"
            )
        } catch (e: Exception) {
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
            val find: ProfileEntity =
                profileRepository.findByUserID(userID) ?: throw BusinessException("Perfil não encontrado")
            if (file != null) {
                bucketProfileRepository.saveImage(userID, file, file.contentType!!)
                image = bucketProfileRepository.getLinkImage(userID)
            }
            val result = profileRepository.save(find.copy(picture = image))
            ResultEntity(
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                total = 1,
                message = "Sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/{userID}/manga")
    @ResponseBody
    fun updateFavorteManga(
        authentication: Authentication,
        @RequestBody body: FavoriteManga,
        @PathVariable userID: String
    ): ResultEntity {
        return try {
            verifyUserIdPermissionService.get(authentication, userID)
            val find = profileRepository.findByUserID(userID) ?: throw BusinessException("Perfil não encontrado")
            val mangas = find.mangasHighlight.toMutableList()
            mangas.removeIf { it.order == body.order }
            mangas.add(body)
            val result = profileRepository.save(
                find.copy(
                    updatedAt = Date().time,
                    mangasHighlight = mangas,
                )
            )
            ResultEntity(listOf(result))
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/{userID}/achievement")
    @ResponseBody
    fun updateFavorteAchievement(
        authentication: Authentication,
        @RequestBody body: FavoriteAchievement,
        @PathVariable userID: String
    ): ResultEntity {
        return try {
            verifyUserIdPermissionService.get(authentication, userID)
            val find = profileRepository.findByUserID(userID) ?: throw BusinessException("Perfil não encontrado")
            val achievements = find.achievementsHighlight.toMutableList()
            achievements.removeIf { it.order == body.order }
            achievements.add(body)
            val result = profileRepository.save(
                find.copy(
                    updatedAt = Date().time,
                    achievementsHighlight = achievements,
                )
            )
            ResultEntity(listOf(result))
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }


}