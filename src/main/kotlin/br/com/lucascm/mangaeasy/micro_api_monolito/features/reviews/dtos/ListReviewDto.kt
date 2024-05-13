package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewMangaEntity
import java.io.Serializable

data class ListReviewDto(
    val id: Long? = null,
    val uniqueid: String = "",
    val userId: String = "",
    val commentary: String? = null,
    val totalLikes: Long = 0,
    val hasSpoiler: Boolean = false,
    val hasUpdated: Boolean = false,
    val value: Double = 0.0,
    val updatedAt: Long? = 0,
    val createdAt: Long? = 0,
    val userName: String? = null,
    var userImage: String? = null,
) : Serializable {
    companion object {
        fun fromEntity(reviewEntity: ReviewMangaEntity, userName: String?, userImage: String?): ListReviewDto {
            return ListReviewDto(
                reviewEntity.id,
                reviewEntity.uniqueid,
                reviewEntity.userId,
                reviewEntity.commentary,
                reviewEntity.totalLikes,
                reviewEntity.hasSpoiler,
                reviewEntity.hasUpdated,
                reviewEntity.value,
                reviewEntity.updatedAt,
                reviewEntity.createdAt,
                userName,
                userImage
            )
        }
    }


}