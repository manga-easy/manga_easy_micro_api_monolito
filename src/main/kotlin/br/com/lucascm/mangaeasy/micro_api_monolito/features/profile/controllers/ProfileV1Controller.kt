package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.FavoriteAchievement
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.FavoriteManga
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileV1Dto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.BucketProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.services.ProfileService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*


@RestController
@RequestMapping("/v1/profile")
@Tag(name = "Profiles")
@Deprecated("Remover na 0.18.0 -> 0.20.0")
class ProfileV1Controller {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @Autowired
    lateinit var bucketProfileRepository: BucketProfileRepository

    @Autowired
    lateinit var profileService: ProfileService

    @GetMapping("/{userID}")
    @ResponseBody
    fun getProfile(@PathVariable userID: String): ResultEntity {
        return try {
            val result = profileService.findByUserId(userID)
            ResultEntity(
                status = StatusResultEnum.SUCCESS,
                data = listOf(ProfileV1Dto.fromEntity(result)),
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
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: ProfileEntity,
        @PathVariable userID: String
    ): ResultEntity {
        return try {
            handlerPermissionUser.handleIsOwnerToken(userAuth, userID)
            val find = profileService.findByUserId(userID)
            val result = profileService.save(
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
                data = listOf(ProfileV1Dto.fromEntity(result)),
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
        @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        return try {
            var image: String? = null
            handlerPermissionUser.handleIsOwnerToken(userAuth, userID)
            val find: ProfileEntity =
                profileService.findByUserId(userID)
            if (file != null) {
                bucketProfileRepository.saveImage(userID, file)
                image = bucketProfileRepository.getLinkImage(userID)
            }
            val result = profileService.save(find.copy(picture = image))
            ResultEntity(
                status = StatusResultEnum.SUCCESS,
                data = listOf(ProfileV1Dto.fromEntity(result)),
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
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: FavoriteManga,
        @PathVariable userID: String
    ): ResultEntity {
        return try {
            handlerPermissionUser.handleIsOwnerToken(userAuth, userID)
            val find = profileService.findByUserId(userID)
            val mangas = find.mangasHighlight.toMutableList()
            mangas.removeIf { it.order == body.order }
            if (body.manga != null) {
                mangas.add(body)
            }
            val result = profileService.save(
                find.copy(
                    updatedAt = Date().time,
                    mangasHighlight = mangas,
                )
            )
            ResultEntity(listOf(ProfileV1Dto.fromEntity(result)))
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/{userID}/achievement")
    @ResponseBody
    fun updateFavorteAchievement(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: FavoriteAchievement,
        @PathVariable userID: String
    ): ResultEntity {
        return try {
            handlerPermissionUser.handleIsOwnerToken(userAuth, userID)
            val find = profileService.findByUserId(userID)
            val achievements = find.achievementsHighlight.toMutableList()
            achievements.removeIf { it.order == body.order }
            if (body.achievement != null) {
                achievements.add(body)
            }
            val result = profileService.save(
                find.copy(
                    updatedAt = Date().time,
                    achievementsHighlight = achievements,
                )
            )
            ResultEntity(listOf(ProfileV1Dto.fromEntity(result)))
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }


}