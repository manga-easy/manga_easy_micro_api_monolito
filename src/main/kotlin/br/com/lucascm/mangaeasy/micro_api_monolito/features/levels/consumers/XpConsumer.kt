package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.consumers

import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.XpRepository
import com.github.sonus21.rqueue.annotation.RqueueListener
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import kotlin.random.Random


@Component
class XpConsumer {
    @Autowired
    lateinit var xpRepository: XpRepository

    @RqueueListener("xp", numRetries = "3")
    fun onMessage(xp: XpConsumerDto) {
        KotlinLogging.logger("onMessage").info("---------- init onMessage ----------------")
        val result = xpRepository.findByUserIDAndUniqueIDAndChapterNumber(
            xp.useId,
            xp.uniqueID,
            xp.chapterNumber
        )
        if (result.isEmpty()) {
            xpRepository.save(
                XpEntity(
                    uniqueID = xp.uniqueID,
                    chapterNumber = xp.chapterNumber,
                    userID = xp.useId,
                    createdAt = Date().time,
                    updatedAt = Date().time,
                    quantity = Random.nextInt(1, 6).toLong(),
                    totalMinutes = 1
                )
            )
//            val profile = profileRepository.findByUserID(userID)
//            if (profile != null) {
//                val totalXp = xpRepository.countXpTotalByUserId(userID)
//                profileRepository.save(profile.copy(totalXp = totalXp!!))
//            }
//            return ResultEntity(listOf(true))
        }
        val xp = result.first()
        if (xp.quantity < 50) {
            xpRepository.save(
                xp.copy(
                    updatedAt = Date().time,
                    totalMinutes = ++xp.totalMinutes,
                    quantity = xp.quantity + Random.nextInt(1, 6).toLong()
                )
            )
            //throw BusinessException("Voçê ja atingiu o maximo de xp para esse capitulo do manga")
        }
        KotlinLogging.logger("onMessage").info("---------- finish onMessage ----------------")
    }
}