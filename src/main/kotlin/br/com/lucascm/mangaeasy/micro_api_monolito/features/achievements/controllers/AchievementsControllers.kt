package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandleExceptions
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerUserAdmin
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.AchievementsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.Date

@RestController
@RequestMapping("/v1/achievements")
class AchievementsControllers(@Autowired val repository: AchievementsRepository) {
    @Autowired lateinit var handleExceptions: HandleExceptions
    @Autowired lateinit var handlerUserAdmin: HandlerUserAdmin
    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam available: Boolean?): ResultEntity {
        try {
            val result: List<AchievementsEntity> = if (available == true){
                repository.findByDisponivelOrderByCreatedatDesc(available)
            }else{
                repository.findAll(Sort.by(Sort.Direction.DESC, "createdat"))
            }
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    fun getOne(@PathVariable  id: String) : ResultEntity {
        try {
            val result = repository.findByUid(id)
            if (result == null){
                throw BusinessException("Emblema não encontrado")
            }
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }

    @PostMapping
    @ResponseBody
    fun create(@RequestBody body: AchievementsEntity, authentication: Authentication) : ResultEntity {
        try {
            handlerUserAdmin.handleIsAdmin(authentication.principal.toString())
            handlerValidateEntity(body)
            val result = repository.save(body.copy(
                uid = GetUidByFeature().get("achievements"),
                createdat = Date().time,
                time_cria = Date().time,
                updatedat = Date().time,
            ))
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Criado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }
    @PutMapping("/{uid}")
    @ResponseBody
    fun update(
        @RequestBody body: AchievementsEntity,
        authentication: Authentication,
        @PathVariable uid: String,
    ) : ResultEntity {
        try {
            handlerUserAdmin.handleIsAdmin(authentication.principal.toString())
            handlerValidateEntity(body)
            val resultFind = repository.findByUid(uid)
            if (resultFind == null){
                throw BusinessException("Emblema não encontrado")
            }
            val result = repository.save(resultFind.copy(
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
            ))
            return ResultEntity(
                total = 1,
                status = StatusResultEnum.SUCCESS,
                data = listOf(result),
                message = "Atualizado com sucesso"
            )
        } catch (e: Exception) {
            return handleExceptions.handleCatch(e)
        }
    }
    private fun handlerValidateEntity(entity: AchievementsEntity){
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
}