package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.CreateAchievementsDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.AchievementsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.BucketAchievementsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/achievement")
class AchievementsController {
    @Autowired
    lateinit var achievementsRepository: AchievementsRepository

    @Autowired
    lateinit var bucketAchievementsRepository: BucketAchievementsRepository


    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
    fun list(@RequestParam available: Boolean?): List<AchievementsEntity> {
        return achievementsRepository.findAll(
            Sort.by(
                Sort.Direction.DESC,
                AchievementsEntity::updatedAt.name
            )
        )
    }

    @GetMapping("/v1/user/{userId}")
    fun listByUser(@PathVariable userId: String): List<AchievementsEntity> {
        return achievementsRepository.findByUser(userId)
    }

    @GetMapping("/v1/{id}")
    fun getOne(@PathVariable id: String): AchievementsEntity {
        return achievementsRepository.findById(id).getOrNull() ?: throw BusinessException("Emblema não encontrado")
    }

    @PostMapping("/v1")
    fun create(
        @RequestBody body: CreateAchievementsDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): AchievementsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        body.handlerValidateEntity()
        return achievementsRepository.save(
            AchievementsEntity(
                createdAt = Date().time,
                updatedAt = Date().time,
                name = body.name,
                benefits = body.benefits,
                category = body.category,
                description = body.description,
                percentRarity = 0.0,
                rarity = body.rarity,
                reclaim = body.reclaim,
                totalAcquired = 0
            )
        )
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun update(
        @RequestBody body: CreateAchievementsDto,
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable id: String,
    ): AchievementsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        body.handlerValidateEntity()
        val resultFind = achievementsRepository.findById(id).getOrNull()
            ?: throw BusinessException("Emblema não encontrado")
        return achievementsRepository.save(
            resultFind.copy(
                updatedAt = Date().time,
                name = body.name,
                benefits = body.benefits,
                category = body.category,
                description = body.description,
                reclaim = body.reclaim,
                rarity = body.rarity
            )
        )
    }

    @PutMapping("/{id}/image")
    fun uploadImage(
        @RequestPart file: MultipartFile,
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ): AchievementsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val find: AchievementsEntity = achievementsRepository.findById(id).getOrNull()
            ?: throw BusinessException("Emblema não encontrado")
        bucketAchievementsRepository.saveImage(id, file, file.contentType!!)
        val imageResult = bucketAchievementsRepository.getLinkImage(id)
        return achievementsRepository.save(find.copy(url = imageResult, updatedAt = Date().time))
    }
}