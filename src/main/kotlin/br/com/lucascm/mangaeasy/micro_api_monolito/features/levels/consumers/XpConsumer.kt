package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.consumers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.messages.QueueName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpConsumerDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.XpRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
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

    @Autowired
    lateinit var profileRepository: ProfileRepository


    @RqueueListener(QueueName.XP, numRetries = "1", concurrency = "1")
    fun onMessage(xp: XpConsumerDto) {
        log.info("---------- onMessage init ----------------")
        log.info("---------- onMessage xp {} ----------------", xp)
        val result = xpRepository.findByUserIDAndUniqueIDAndChapterNumber(
            xp.userId,
            xp.uniqueID,
            xp.chapterNumber
        )
        log.info("---------- findByUserIDAndUniqueIDAndChapterNumber {} ----------------", result)
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
            log.info("---------- xpRepository.save {} ----------------", result)
            updateTotalXp(xp.userId)
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
            log.info("---------- xpRepository.save {} ----------------", result)
            updateTotalXp(xp.userId)
        }
        log.info("---------- onMessage finish quantity: {} ----------------", resultFirst.quantity)
    }

    private fun updateTotalXp(userId: String) {
        val profile = profileService.findByUserId(userId)
        val totalXp = xpRepository.countXpTotalByUserId(userId)
        profileRepository.save(profile.copy(totalXp = totalXp!!))
        log.info("---------- profileRepository.save  ----------------")
    }
}