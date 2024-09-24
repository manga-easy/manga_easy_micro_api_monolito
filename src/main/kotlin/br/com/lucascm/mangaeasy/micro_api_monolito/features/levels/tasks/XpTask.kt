package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.tasks

import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.RankingEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.RankingRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import kotlin.time.measureTimedValue

@Component
class XpTask {

    @Autowired
    lateinit var rankingRepository: RankingRepository

    @Autowired
    lateinit var profileRepository: ProfileRepository
    val log = LoggerFactory.getLogger(XpTask::class.java)

    @Scheduled(cron = "0 0 4 * * *")
    fun updateRanking() {
        log.debug("------------------ inicia updateRanking --------------")
        var place: Long = 0
        var offset: Long = 0
        val time = measureTimedValue {
            while (true) {
                val xp = profileRepository.countXpRanking(offset * 100)
                log.debug("------------------ xp total: {} --------------", xp.size)
                if (xp.isEmpty()) break
                for (i in xp) {
                    val userId = i["user_id"].toString()
                    saveRanking(
                        RankingEntity(
                            totalXp = i["total_xp"] as Long,
                            name = i["name"] as String?,
                            picture = i["picture"] as String?,
                            place = ++place,
                            userId = userId
                        )
                    )

                }
                ++offset
            }
        }
        log.debug("------------------ finaliza updateRanking time: {} --------------", time.duration.inWholeMinutes)
    }

    private fun saveRanking(rankingEntity: RankingEntity) {
        val find = rankingRepository.findByPlace(rankingEntity.place)
        if (find == null) {
            rankingRepository.save(rankingEntity)
            return
        }
        rankingRepository.save(
            find.copy(
                totalXp = rankingEntity.totalXp,
                name = rankingEntity.name,
                picture = rankingEntity.picture,
                userId = rankingEntity.userId
            )
        )
    }
}