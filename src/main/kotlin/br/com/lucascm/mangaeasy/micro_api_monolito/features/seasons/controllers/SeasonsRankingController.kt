package br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.ResultEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.StatusResultEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.entities.SeasonsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.repositories.SeasonsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities.UsersLevelsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersLevelsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/seasons")
class SeasonsRankingController(@Autowired val repository: UsersLevelsRepository,
                               @Autowired val seasonsRepository: SeasonsRepository) {
    @GetMapping("/{idSeason}/ranking")
    @ResponseBody
    fun list(@PathVariable idSeason : String) : ResultEntity<UsersLevelsEntity> {
        try {
            val resultSeasons = seasonsRepository.findByUid(idSeason)
            if (resultSeasons == null){
                throw Exception("Temporada não existe")
            }
            val result: List<UsersLevelsEntity> = repository.findTop25ByTemporadaOrderByTotalDesc(idSeason)
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