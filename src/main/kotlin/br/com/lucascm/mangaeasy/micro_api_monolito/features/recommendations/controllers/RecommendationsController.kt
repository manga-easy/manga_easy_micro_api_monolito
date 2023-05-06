package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories.RecommendationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/recommendations")
class RecommendationsController(@Autowired val repository: RecommendationsRepository) {
    @GetMapping("/list")
    @ResponseBody
    fun list(@RequestParam status : String?,
             @RequestParam idhost : Int?
    ) : ResultEntity<RecommendationsEntity> {
        try {
            val result: List<RecommendationsEntity> = repository.findAll()
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