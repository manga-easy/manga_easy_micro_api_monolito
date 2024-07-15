package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewEntity

data class ListReviewDto(
    val reviewEntity: ReviewEntity,
    val userName: String? = null,
    var userImage: String? = null,
)