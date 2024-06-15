package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.tasks

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

    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.HOURS)
    fun updateRanking() {

        log.info("------------------ finaliza updateRanking --------------")
    }
}