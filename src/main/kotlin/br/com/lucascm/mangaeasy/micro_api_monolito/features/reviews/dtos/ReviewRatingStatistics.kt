package br.com.lucascm.mangaeasy.micro_api_monolito.features.reviews.dtos

data class ReviewRatingStatistics(
    val rating: Double = 0.0,
    val quantity: Long = 0,
    val quantity1: Long = 0,
    val quantity2: Long = 0,
    val quantity3: Long = 0,
    val quantity4: Long = 0,
    val quantity5: Long = 0,
    val rating1: Double = 0.0,
    val rating2: Double = 0.0,
    val rating3: Double = 0.0,
    val rating4: Double = 0.0,
    val rating5: Double = 0.0,
)