package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.services

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.RedisCacheName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories.XpRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.repositories.LibrariesRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories.ProfileV1Repository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UserRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class ProfileService {
    @Autowired
    lateinit var profileRepository: ProfileRepository

    @Autowired
    lateinit var profileV1Repository: ProfileV1Repository

    @Autowired
    lateinit var usersAchievementsRepository: UsersAchievementsRepository

    @Autowired
    lateinit var librariesRepository: LibrariesRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var xpRepository: XpRepository

    @Cacheable(value = [RedisCacheName.PROFILE], key = "#userId")
    fun findByUserId(userId: String): ProfileEntity {
        var profile = profileRepository.findByUserId(userId)
        if (profile == null) {
            val resultV1 = profileV1Repository.findByUserID(userId)
            profile = resultV1?.toV2()
            if (profile != null) return profileRepository.save(profile)
        }
        if (profile == null) {
            profile = createProfile(userId)
            return profileRepository.save(profile)
        }
        return updateTotals(profile)
    }

    fun findById(id: String): ProfileEntity? {
        val result = profileRepository.findById(id)
        if (result.isPresent) {
            return updateTotals(result.get())
        }
        val resultV1 = profileV1Repository.findById(id)
        if (resultV1.isPresent) {
            return updateTotals(resultV1.get().toV2())
        }
        return null
    }

    @CacheEvict(value = [RedisCacheName.PROFILE], key = "#profile.userId")
    fun save(profile: ProfileEntity): ProfileEntity {
        return profileRepository.save(profile)
    }

    private fun updateTotals(profile: ProfileEntity): ProfileEntity {
        val totalMangaRead = librariesRepository.countByStatusAndUserId(profile.userId)
        val totalAchievements = usersAchievementsRepository.countByUserId(profile.userId)
        val totalXp = xpRepository.countXpTotalByUserId(profile.userId)
        return profile.copy(
            totalMangaRead = totalMangaRead,
            totalAchievements = totalAchievements,
            totalXp = totalXp ?: 0,
        )
    }

    private fun createProfile(userId: String): ProfileEntity {
        val user = userRepository.getId(userId)

        return ProfileEntity(
            updatedAt = Date().time,
            biography = "",
            createdAt = Date.from(Instant.parse(user.registration)).time,
            achievementsHighlight = listOf(),
            mangasHighlight = listOf(),
            userId = userId,
            totalMangaRead = 0,
            totalAchievements = 0,
            role = "Aventureiro",
            totalXp = 0,
        )
    }
}