package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.tasks

import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.RankingEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.RankingCache
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.XpRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class XpTask {
    @Autowired
    lateinit var xpRepository: XpRepository
    @Autowired
    lateinit var rankingCache: RankingCache
    @Autowired
    lateinit var profileRepository: ProfileRepository
    val log = LoggerFactory.getLogger(XpTask::class.java)
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 10, initialDelay = 1)
    fun updateRanking(){
        log.info("------------------ inicia task --------------")
        var place: Long = 0
        rankingCache.deleteAll()
        while (true){
            val xp = xpRepository.countXpRanking(place * 100)
            if (xp.isEmpty()) break
            log.info("------ ${xp.size}")
            for (i in xp) {
                val profile = profileRepository.findByUserID(i["userId"].toString())
                if (profile == null) continue
                rankingCache.save(
                    RankingEntity(
                        id = profile.userID,
                        totalXp = i["Total"] as Long,
                        name = profile.name,
                        picture = profile.picture,
                        place = ++place
                    )
                )
            }
        }
        log.info("------------------ finaliza task --------------")
    }
}