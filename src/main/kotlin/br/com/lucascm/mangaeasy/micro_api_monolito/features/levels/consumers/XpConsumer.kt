package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.consumers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.QueueName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.XpRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.services.ProfileService
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
    lateinit var profileService: ProfileService
    val log = KotlinLogging.logger("XpConsumer")

    @RqueueListener(QueueName.XP, numRetries = "3", concurrency = "1")
    fun onMessage(xp: XpConsumerDto) {
        log.warn("---------- onMessage init ----------------")
        log.warn("---------- onMessage xp {} ----------------", xp)
        val result = xpRepository.findByUserIDAndUniqueIDAndChapterNumber(
            xp.userId,
            xp.uniqueID,
            xp.chapterNumber
        )
        if (result.isEmpty()) {
            xpRepository.save(
                XpEntity(
                    uniqueID = xp.uniqueID,
                    chapterNumber = xp.chapterNumber,
                    userID = xp.userId,
                    createdAt = Date().time,
                    updatedAt = Date().time,
                    quantity = Random.nextInt(1, 6).toLong(),
                    totalMinutes = 1
                )
            )
            updateTotalXp(xp.userId)
            return
        }
        val resultFirst = result.first()
        if (resultFirst.quantity < 50) {
            updateTotalXp(xp.userId)
            xpRepository.save(
                resultFirst.copy(
                    updatedAt = Date().time,
                    totalMinutes = ++resultFirst.totalMinutes,
                    quantity = resultFirst.quantity + Random.nextInt(1, 6).toLong()
                )
            )
        }
        log.warn("---------- onMessage finish quantity: {} ----------------", resultFirst.quantity)
    }

    private fun updateTotalXp(userId: String) {
        val profile = profileService.findByUserId(userId)
        val totalXp = xpRepository.countXpTotalByUserId(userId)
        profileService.save(profile.copy(totalXp = totalXp!!))

    }
}