package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsV1Dto
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
@RequestMapping("/v1/achievements")
@Deprecated("Remover 0.18 -> 0.20")
class AchievementsV1Controller {

    @Autowired
    lateinit var achievementsRepository: AchievementsRepository

    @Autowired
    lateinit var bucketAchievementsRepository: BucketAchievementsRepository

    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam available: Boolean?): ResultEntity {
        return try {
            val result: List<AchievementsEntity> = if (available == true) {
                achievementsRepository.findByReclaimOrderByCreatedAtDesc(available)
            } else {
                achievementsRepository.findAll(Sort.by(Sort.Direction.DESC, AchievementsEntity::updatedAt.name))
            }
            ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result.map { AchievementsV1Dto.fromEntity(it) }.toList(),
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    fun listByUser(@PathVariable userId: String): ResultEntity {
        return try {
            val result = achievementsRepository.findByUser(userId)
            ResultEntity(result.map { AchievementsV1Dto.fromEntity(it) }.toList())
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    fun getOne(@PathVariable id: String): ResultEntity {
        return try {
            val result = achievementsRepository.findById(id).getOrNull()
                ?: throw BusinessException("Emblema não encontrado")
            ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(AchievementsV1Dto.fromEntity(result)),
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PostMapping
    @ResponseBody
    fun create(@RequestBody body: CreateAchievementsDto, @AuthenticationPrincipal userAuth: UserAuth): ResultEntity {
        return try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            body.handlerValidateEntity()
            val result = achievementsRepository.save(
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
            ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(AchievementsV1Dto.fromEntity(result)),
                message = "Criado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/{uid}")
    @ResponseBody
    fun update(
        @RequestBody body: CreateAchievementsDto,
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable uid: String,
    ): ResultEntity {
        return try {
            handlerPermissionUser.handleIsAdmin(userAuth)
            body.handlerValidateEntity()
            val resultFind = achievementsRepository.findById(uid).getOrNull()
                ?: throw BusinessException("Emblema não encontrado")
            val result = achievementsRepository.save(
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
            ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(AchievementsV1Dto.fromEntity(result)),
                message = "Atualizado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/{uid}/image")
    fun uploadImage(
        @RequestPart file: MultipartFile?, @PathVariable uid: String, @AuthenticationPrincipal userAuth: UserAuth
    ): ResultEntity {
        return try {
            var imageResult: String? = null
            handlerPermissionUser.handleIsAdmin(userAuth)
            val find: AchievementsEntity = achievementsRepository.findById(uid).getOrNull()
                ?: throw BusinessException("Emblema não encontrado")
            if (file != null) {
                bucketAchievementsRepository.saveImage(uid, file, file.contentType!!)
                imageResult = bucketAchievementsRepository.getLinkImage(uid)
            }
            val result = achievementsRepository.save(find.copy(url = imageResult!!))
            ResultEntity(
                status = StatusResultEnum.SUCCESS,
                data = listOf(AchievementsV1Dto.fromEntity(result)),
                total = 1,
                message = "Sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }


}