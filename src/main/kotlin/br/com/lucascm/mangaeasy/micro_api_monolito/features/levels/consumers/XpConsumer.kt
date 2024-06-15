package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.consumers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.QueueName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.XpRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
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

    @Autowired
    lateinit var profileRepository: ProfileRepository
    val log = KotlinLogging.logger("XpConsumer")

    @RqueueListener(QueueName.xp, numRetries = "3")
    fun onMessage(xp: XpConsumerDto) {
        log.info("---------- onMessage init ----------------")
        log.info("---------- onMessage xp {} ----------------", xp)
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
            val profile = profileRepository.findByUserID(xp.useId)
            if (profile != null) {
                val totalXp = xpRepository.countXpTotalByUserId(xp.useId)
                profileRepository.save(profile.copy(totalXp = totalXp!!))
            }
            return
        }
        val resultFirst = result.first()
        if (resultFirst.quantity < 50) {
            xpRepository.save(
                resultFirst.copy(
                    updatedAt = Date().time,
                    totalMinutes = ++resultFirst.totalMinutes,
                    quantity = resultFirst.quantity + Random.nextInt(1, 6).toLong()
                )
            )
        }
        log.info("---------- onMessage finish ----------------")
    }
}