package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.AchievementsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/achievements")
class AchievementsControllers(@Autowired val repository: AchievementsRepository) {
    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam available: Boolean?) : ResultEntity<AchievementsEntity> {
        try {
            val result: List<AchievementsEntity> = if (available == true){
                repository.findAllByDisponivel(available)
            }else{
                repository.findAll()
            }
            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return ResultEntity(
                total = 0,
                status = StatusResultEnum.ERROR,
                data = listOf(),
                message = e.message
            )
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    fun getOne(@PathVariable  id: String) : ResultEntity<AchievementsEntity> {
        try {
            val result: List<AchievementsEntity> = repository.findAllByUid(id)

            return ResultEntity(
                total = result.size,
                status = StatusResultEnum.SUCCESS,
                data = result,
                message = "Listado com sucesso"
            )
        } catch (e: Exception) {
            return ResultEntity(
                total = 0,
                status = StatusResultEnum.ERROR,
                data = listOf(),
                message = e.message
            )
        }
    }
}