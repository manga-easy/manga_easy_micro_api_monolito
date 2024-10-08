package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.FavoriteAchievement
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.FavoriteManga
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.UpdateProfileDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.BucketProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.services.ProfileService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*


@RestController
@RequestMapping("/profiles")
@Tag(name = "Profiles")
class ProfileController {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var bucketProfileRepository: BucketProfileRepository

    @Autowired
    lateinit var profileService: ProfileService

    val notFoundMessage: String = "Perfil não encontrado"


    @GetMapping("/v1/users/{userId}")
    fun getProfileByUser(@PathVariable userId: String): ProfileEntity {
        return profileService.findByUserId(userId)
    }

    @GetMapping("/v1/{id}")
    fun getProfileById(@PathVariable id: String): ProfileEntity {
        return profileService.findById(id)
            ?: throw BusinessException(notFoundMessage)
    }

    @PutMapping("/v1/{id}")
    fun updateProfile(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: UpdateProfileDto,
        @PathVariable id: String
    ): ProfileEntity {
        val find = getProfile(id, userAuth)
        if (body.name == null || body.name.trim().isEmpty()) {
            throw BusinessException("Nome não pode ser vazio")
        }
        return profileService.save(
            find.copy(
                biography = body.biography,
                updatedAt = Date().time,
                name = body.name,
                visibleStatics = body.visibleStatics,
                visibleAchievements = body.visibleAchievements,
                visibleMangas = body.visibleMangas
            )
        )
    }

    @PutMapping("/v1/{id}/images")
    fun handleFileUpload(
        @RequestPart file: MultipartFile?,
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable id: String
    ): ProfileEntity {
        var image: String? = null
        val find = getProfile(id, userAuth)
        if (file != null) {
            bucketProfileRepository.saveImage(find.userId, file)
            image = bucketProfileRepository.getLinkImage(find.userId)
        }
        return profileService.save(find.copy(picture = image))
    }

    @PutMapping("/v1/{id}/mangas")
    @ResponseBody
    fun updateFavorteManga(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: FavoriteManga,
        @PathVariable id: String
    ): ProfileEntity {
        val find = getProfile(id, userAuth)
        val mangas = find.mangasHighlight.toMutableList()
        mangas.removeIf { it.order == body.order }
        if (body.manga != null) {
            mangas.add(body)
        }
        return profileService.save(
            find.copy(
                updatedAt = Date().time,
                mangasHighlight = mangas,
            )
        )
    }

    @PutMapping("/v1/{id}/achievements")
    @ResponseBody
    fun updateFavorteAchievement(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: FavoriteAchievement,
        @PathVariable id: String
    ): ProfileEntity {
        val find = getProfile(id, userAuth)
        val achievements = find.achievementsHighlight.toMutableList()
        achievements.removeIf { it.order == body.order }
        if (body.achievement != null) {
            achievements.add(body)
        }
        return profileService.save(
            find.copy(
                updatedAt = Date().time,
                achievementsHighlight = achievements,
            )
        )
    }

    private fun getProfile(id: String, userAuth: UserAuth): ProfileEntity {
        val find = profileService.findById(id)
            ?: throw BusinessException(notFoundMessage)
        handlerPermissionUser.handleIsOwnerToken(userAuth, find.userId)
        return find
    }
}