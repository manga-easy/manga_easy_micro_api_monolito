package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.dtos

import jakarta.persistence.Column

data class BannerDtoV1(
    val uid: String? = null,
    val image: String = "",
    val link: String = "",
    val createdat: Long? = null,
    val type: String = "",
    val updatedat: Long? = null,
)