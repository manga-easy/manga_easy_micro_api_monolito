package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.tasks

import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.RankingEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.RankingRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.XpRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import kotlin.time.measureTimedValue

@Component
class XpTask {
    @Autowired
    lateinit var xpRepository: XpRepository

    @Autowired
    lateinit var rankingRepository: RankingRepository

    @Autowired
    lateinit var profileRepository: ProfileRepository
    val log = LoggerFactory.getLogger(XpTask::class.java)

    @Scheduled(fixedRate = 12, timeUnit = TimeUnit.HOURS)
    fun updateRanking() {
        log.info("------------------ inicia updateRanking --------------")
        var place: Long = 0
        var offset: Long = 0
        val time = measureTimedValue {
            while (true) {
                val xp = xpRepository.countXpRanking(offset * 100)
                if (xp.isEmpty()) break
                log.info("---------- ${xp.size}")
                for (i in xp) {
                    val userId = i["userId"].toString()
                    val profile = profileRepository.findByUserID(userId)
                    saveRanking(
                        RankingEntity(
                            totalXp = i["Total"] as Long,
                            name = profile?.name,
                            picture = profile?.picture,
                            place = ++place,
                            userId = userId
                        )
                    )

                }
                ++offset
            }
        }
        log.info("------------------ finaliza updateRanking time: {} --------------", time.duration.inWholeMinutes)
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