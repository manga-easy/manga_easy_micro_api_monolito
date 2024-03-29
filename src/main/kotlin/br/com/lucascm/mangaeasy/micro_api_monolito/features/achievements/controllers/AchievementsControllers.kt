package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerUserAdmin
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.AchievementsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.BucketAchievementsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.controllers.TYPE_CONTENT_IMAGE
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

const val LIMIT_FILE_SIZE_ACHIEVEMENT = 500000

@RestController
@RequestMapping("/v1/achievements")
class AchievementsControllers {
    @Autowired
    lateinit var achievementsRepository: AchievementsRepository

    @Autowired
    lateinit var bucketAchievementsRepository: BucketAchievementsRepository

    @Autowired
    lateinit var handleExceptions: HandleExceptions

    @Autowired
    lateinit var handlerUserAdmin: HandlerUserAdmin

    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam available: Boolean?): ResultEntity {
        return try {
            val result: List<AchievementsEntity> = if (available == true) {
                achievementsRepository.findByDisponivelOrderByCreatedatDesc(available)
            } else {
                achievementsRepository.findAll(Sort.by(Sort.Direction.DESC, "createdat"))
            }
            ResultEntity(
                total = result.size, status = StatusResultEnum.SUCCESS, data = result, message = "Listado com sucesso"
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
            ResultEntity(result)
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    fun getOne(@PathVariable id: String): ResultEntity {
        return try {
            val result = achievementsRepository.findByUid(id) ?: throw BusinessException("Emblema não encontrado")
            ResultEntity(
                total = 1, status = StatusResultEnum.SUCCESS, data = listOf(result), message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PostMapping
    @ResponseBody
    fun create(@RequestBody body: AchievementsEntity, authentication: Authentication): ResultEntity {
        return try {
            handlerUserAdmin.handleIsAdmin(authentication.principal.toString())
            handlerValidateEntity(body)
            val result = achievementsRepository.save(
                body.copy(
                    uid = GetUidByFeature().get("achievements"),
                    createdat = Date().time,
                    time_cria = Date().time,
                    updatedat = Date().time,
                )
            )
            ResultEntity(
                total = 1, status = StatusResultEnum.SUCCESS, data = listOf(result), message = "Criado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/{uid}")
    @ResponseBody
    fun update(
        @RequestBody body: AchievementsEntity,
        authentication: Authentication,
        @PathVariable uid: String,
    ): ResultEntity {
        return try {
            handlerUserAdmin.handleIsAdmin(authentication.principal.toString())
            handlerValidateEntity(body)
            val resultFind = achievementsRepository.findByUid(uid) ?: throw BusinessException("Emblema não encontrado")
            val result = achievementsRepository.save(
                resultFind.copy(
                    updatedat = Date().time,
                    name = body.name,
                    type = body.type,
                    adsoff = body.adsoff,
                    benefits = body.benefits,
                    categoria = body.categoria,
                    description = body.description,
                    disponivel = body.disponivel,
                    percent = body.percent,
                    rarity = body.rarity,
                    url = body.url,
                )
            )
            ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Atualizado com sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    @PutMapping("/{uid}/image")
    fun uploadImage(
        @RequestPart file: MultipartFile?, @PathVariable uid: String, authentication: Authentication
    ): ResultEntity {
        return try {
            var imageResult: String? = null
            handlerUserAdmin.handleIsAdmin(authentication.principal.toString())
            val find: AchievementsEntity =
                achievementsRepository.findByUid(uid) ?: throw BusinessException("Emblema não encontrado")
            if (file != null) {
                validateImage(file)
                bucketAchievementsRepository.saveImage(uid, file, file.contentType!!)
                imageResult = bucketAchievementsRepository.getLinkImage(uid)
            }
            val result = achievementsRepository.save(find.copy(url = imageResult!!))
            ResultEntity(
                status = StatusResultEnum.SUCCESS, data = listOf(result), total = 1, message = "Sucesso"
            )
        } catch (e: Exception) {
            handleExceptions.handleCatch(e)
        }
    }

    private fun handlerValidateEntity(entity: AchievementsEntity) {
        // Verificar disponibilidade
        if ((entity.categoria == "doacao" || entity.categoria == "rank") && entity.disponivel) {
            throw BusinessException("Esse tipo de emblema não pode estar disponível")
        }
        if (entity.categoria != "doacao" && entity.adsoff) {
            throw BusinessException("Esse tipo de emblema não pode remover propaganda")
        }
        // Validar campos obrigatórios
        validateNonEmptyField(entity.categoria, "categoria")
        validateNonEmptyField(entity.benefits, "benefícios")
        validateNonEmptyField(entity.description, "descrição")
        validateNonEmptyField(entity.url, "url")
        validateNonEmptyField(entity.name, "nome")
        validateNonEmptyField(entity.rarity, "raridade")
        validateNonEmptyField(entity.type, "tipo")
    }

    private fun validateNonEmptyField(campo: String, nomeCampo: String) {
        if (campo.isEmpty()) {
            throw BusinessException(String.format("O campo %s não pode ser vazio", nomeCampo))
        }
    }

    private fun validateImage(file: MultipartFile) {
        val limit = LIMIT_FILE_SIZE_ACHIEVEMENT
        if (file.size > limit) throw BusinessException("Imagem maior que o permitido: ${limit.toString()[0]}mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) throw BusinessException("Tipo de arquivo não permitido.")
    }
}