package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.services

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessCode
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.RedisCacheName
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.AchievementsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.services.ProfileService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ListReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ReviewDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos.ReviewRatingStatistics
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.ReviewLikeRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.repositories.ReviewRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class ReviewService {
    companion object {
        const val COPPER = "61b12f7a0ff25"
        const val SILVER = "61b12fddd7201"
        const val GOLD = "61b1302ef0d76"
        const val PLATINUM = "61b130768bd07"
        const val IRON = "627daca5e13281a2e330"
    }

    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var profileService: ProfileService

    @Autowired
    lateinit var likeReviewRepository: ReviewLikeRepository

    @Autowired
    lateinit var usersAchievementsRepository: UsersAchievementsRepository

    @Autowired
    lateinit var achievementsRepository: AchievementsRepository

    @Cacheable(RedisCacheName.LIST_REVIEW)
    fun list(catalogId: String, page: Int): List<ListReviewDto> {
        val result = reviewRepository.findByCatalogIdAndCommentaryIsNotNull(
            catalogId,
            PageRequest.of(page, 25)
        )
        return getInfo(result)
    }

    fun list(page: Int): List<ListReviewDto> {
        val result = reviewRepository.findAll(
            PageRequest.of(
                page,
                25,
                Sort.by(ReviewEntity::updatedAt.name).descending()
            ),
        )
        return getInfo(result.content)
    }

    @Cacheable(RedisCacheName.LIST_REVIEW_LAST)
    fun listLast(catalogId: String): List<ListReviewDto> {
        val result = reviewRepository.findTop10ByCatalogIdAndCommentaryIsNotNullOrderByCreatedAtDesc(catalogId)
        return getInfo(result)
    }

    @Cacheable(RedisCacheName.REVIEW_RATING_STATISTICS, key = "#catalogId")
    fun ratingStatisticsByCatalog(catalogId: String): ReviewRatingStatistics {
        val review = reviewRepository.countReviewsByCatalog(catalogId)

        if (review.toInt() == 0) throw BusinessException("Obra não tem avaliações")

        val result = reviewRepository.ratingStatisticsByCatalog(catalogId)
        return ReviewRatingStatistics(
            rating = result["rating"] as Double,
            quantity = result["quantity"].toString().toLong(),
            quantity1 = result["quantity1"].toString().toLong(),
            quantity2 = result["quantity2"].toString().toLong(),
            quantity3 = result["quantity3"].toString().toLong(),
            quantity4 = result["quantity4"].toString().toLong(),
            quantity5 = result["quantity5"].toString().toLong(),
            rating1 = result["rating1"] as Double,
            rating2 = result["rating2"] as Double,
            rating3 = result["rating3"] as Double,
            rating4 = result["rating4"] as Double,
            rating5 = result["rating5"] as Double
        )
    }

    fun findByCatalogIdAndUserId(
        catalogId: String,
        userId: String,
    ): ReviewEntity? {
        val result = reviewRepository.findByCatalogIdAndUserId(catalogId, userId)
            ?: return null
        return updateTotals(result)

    }

    fun create(body: ReviewDto, catalogId: String, userId: String): ReviewEntity {
        val profile = profileService.findByUserId(userId)
        if (profile.name == null || profile.name.trim().isEmpty()) {
            throw BusinessException(
                "Defina um nome no seu perfil para poder realizar uma avaliação",
                BusinessCode.NOT_FOUND_NAME_PROFILE
            )
        }
        val review = reviewRepository.findByCatalogIdAndUserId(catalogId, userId)
        if (review != null) {
            throw BusinessException(
                "Você já avaliou essa obra! As avaliações são contabilizadas em cerca de duas horas."
            )
        }

        return reviewRepository.save(
            ReviewEntity(
                catalogId = catalogId,
                createdAt = Date().time,
                totalLikes = 0,
                userId = userId,
                updatedAt = Date().time,
                commentary = body.commentary,
                rating = body.rating,
                hasSpoiler = body.hasSpoiler,
                hasUpdated = false
            )
        )
    }

    fun update(body: ReviewDto, id: String): ReviewEntity {
        val review = reviewRepository.findById(id).getOrNull()
            ?: throw BusinessException("Avaliação não encontrada")
        return reviewRepository.save(
            review.copy(
                commentary = body.commentary,
                rating = body.rating,
                updatedAt = Date().time,
                hasSpoiler = body.hasSpoiler,
                hasUpdated = true
            )
        )
    }

    private fun getInfo(result: List<ReviewEntity>): List<ListReviewDto> {
        val list = mutableListOf<ListReviewDto>()
        for (review in result) {
            val profile = profileService.findByUserId(review.userId)
            val achievement = getAchievementDonate(review.userId)
            list.add(
                ListReviewDto(
                    review = updateTotals(review),
                    userImage = profile.picture,
                    userName = profile.name,
                    achievementId = achievement?.id,
                    achievementImage = achievement?.url
                )
            )
        }
        return list
    }

    private fun updateTotals(review: ReviewEntity): ReviewEntity {
        val totalLikes = likeReviewRepository.countByReviewId(review.id!!)
        return reviewRepository.save(review.copy(totalLikes = totalLikes))
    }

    private fun getAchievementDonate(userId: String): AchievementsEntity? {
        val achievements = usersAchievementsRepository.findAllByUserId(userId)
        if (achievements.isEmpty()) {
            return null
        }
        var achievementId: String? = null
        for (achievement in achievements) {
            if (achievement.achievementId == PLATINUM) {
                achievementId = achievement.achievementId
                break
            }
            if (achievement.achievementId == GOLD) {
                achievementId = achievement.achievementId
                break
            }
            if (achievement.achievementId == SILVER) {
                achievementId = achievement.achievementId
                break
            }
            if (achievement.achievementId == IRON) {
                achievementId = achievement.achievementId
                break
            }
            if (achievement.achievementId == COPPER) {
                achievementId = achievement.achievementId
                break
            }
        }
        if (achievementId == null) {
            return null
        }
        val response = achievementsRepository.findById(achievementId)
        return response.getOrNull()
    }
}