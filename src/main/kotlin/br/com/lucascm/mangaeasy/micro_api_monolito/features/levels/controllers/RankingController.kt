package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.RankingEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.RankingRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ranking")
@Tag(name = "Ranking")
class RankingController {
    @Autowired
    lateinit var rankingRepository: RankingRepository

    @GetMapping("/v1")
    fun getRanking(@RequestParam page: Int?): List<RankingEntity> {
        return rankingRepository.findAll(
            PageRequest.of(page ?: 0, 25)
                .withSort(Sort.by("place"))
        ).get().toList()
    }

    @GetMapping("/v1/user/{userId}")
    fun getRankingByUser(@PathVariable userId: String): RankingEntity {
        return rankingRepository.findByUserId(userId)
            ?: throw BusinessException("Ranking não encontrado ou em processamento")
    }

}