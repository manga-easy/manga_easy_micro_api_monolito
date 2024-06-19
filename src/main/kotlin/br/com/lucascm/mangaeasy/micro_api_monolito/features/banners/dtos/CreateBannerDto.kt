package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.dtos

data class CreateBannerDto(
    var image: String = "",
    var link: String = ""
)