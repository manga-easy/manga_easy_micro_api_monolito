package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.tasks

import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.FavoriteAchievement
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileV1Repository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MigrateProfileV2 {
    @Autowired
    lateinit var profileRepository: ProfileRepository

    @Autowired
    lateinit var profileV1Repository: ProfileV1Repository
    val log = LoggerFactory.getLogger(MigrateProfileV2::class.java)

    @Scheduled(cron = "0 0 4 * * *")
    fun run() {
        log.info("------------------ inicia MigrateProfileV2 --------------")
        val profiles = profileV1Repository.findByIsV2(null)
        log.info("total de perfil: {}", profiles.size)
        if (profiles.isEmpty()) return
        profiles.forEach {
            try {
                profileRepository.save(
                    ProfileEntity(
                        name = it.name,
                        userId = it.userID,
                        totalMangaRead = it.totalMangaRead,
                        role = it.role,
                        biography = it.biography,
                        mangasHighlight = it.mangasHighlight,
                        visibleStatics = it.visibleStatics,
                        totalAchievements = it.totalAchievements,
                        achievementsHighlight = it.achievementsHighlight.map { its ->
                            FavoriteAchievement(
                                order = its.order,
                                achievement = its.achievement?.toEntity()
                            )
                        }.toList(),
                        visibleAchievements = it.visibleAchievements,
                        visibleMangas = it.visibleMangas,
                        totalXp = it.totalXp,
                        picture = it.picture,
                        updatedAt = it.updatedAt ?: 0,
                        createdAt = it.createdAt ?: 0
                    )
                )
                profileV1Repository.save(it.copy(isV2 = true))
            } catch (e: Exception) {
                log.trace(e.message, e)
            }
        }
    }
}