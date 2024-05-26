package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos

data class ReviewDto(
    val commentary: String? = null,
    val rating: Double = 0.0,
    val hasSpoiler: Boolean = false,
)