package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.entities.ReviewEntity

data class ListReviewDto(
    val review: ReviewEntity? = null,
    val userName: String? = null,
    var userImage: String? = null,
)