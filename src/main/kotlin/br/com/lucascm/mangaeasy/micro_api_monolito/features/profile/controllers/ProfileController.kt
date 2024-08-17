package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.repositories.LibrariesRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.FavoriteAchievement
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.FavoriteManga
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.UpdateProfileDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.BucketProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UserRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/profiles")
@Tag(name = "Profiles")
class ProfileController {
    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @Autowired
    lateinit var profileRepository: ProfileRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var bucketProfileRepository: BucketProfileRepository

    @Autowired
    lateinit var usersAchievementsRepository: UsersAchievementsRepository

    @Autowired
    lateinit var librariesRepository: LibrariesRepository

    val notFoudMessage: String = "Perfil não encontrado"


    @GetMapping("/v1/{userId}")
    fun getProfileByUser(@PathVariable userId: String): ProfileEntity {
        var result = profileRepository.findByUserId(userId)
        if (result == null) {
            val user = userRepository.getId(userId)
            val totalMangaRead = librariesRepository.countByStatusAndUserId(userId)
            val totalAchievements = usersAchievementsRepository.countByUserId(userId)
            result = ProfileEntity(
                updatedAt = Date().time,
                biography = "",
                createdAt = Date.from(Instant.parse(user.registration)).time,
                achievementsHighlight = listOf(),
                mangasHighlight = listOf(),
                userId = userId,
                totalMangaRead = totalMangaRead,
                totalAchievements = totalAchievements,
                role = "Aventureiro",
                totalXp = 0,
            )
            return profileRepository.save(result)
        }
        return result
    }

    @GetMapping("/v1/{id}")
    fun getProfileById(@PathVariable id: String): ProfileEntity {
        return profileRepository.findById(id).getOrNull()
            ?: throw BusinessException(notFoudMessage)
    }

    @PutMapping("/v1/{id}")
    fun updateProfile(
        @AuthenticationPrincipal userAuth: UserAuth,
        @RequestBody body: UpdateProfileDto,
        @PathVariable id: String
    ): ProfileEntity {
        val find = getProfile(id, userAuth)
        return profileRepository.save(
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
        return profileRepository.save(find.copy(picture = image))
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
        return profileRepository.save(
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
        return profileRepository.save(
            find.copy(
                updatedAt = Date().time,
                achievementsHighlight = achievements,
            )
        )
    }

    private fun getProfile(id: String, userAuth: UserAuth): ProfileEntity {
        val find = profileRepository.findById(id).getOrNull()
            ?: throw BusinessException(notFoudMessage)
        handlerPermissionUser.handleIsOwnerToken(userAuth, find.userId)
        return find
    }
}