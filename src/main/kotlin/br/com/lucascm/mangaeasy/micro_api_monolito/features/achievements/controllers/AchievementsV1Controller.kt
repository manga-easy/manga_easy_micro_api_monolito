package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsV1Dto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.AchievementsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/v1/achievements")
@Deprecated("Remover 0.18 -> 0.20")
@Tag(name = "Achievements")
class AchievementsV1Controller {

    @Autowired
    lateinit var achievementsRepository: AchievementsRepository

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
}