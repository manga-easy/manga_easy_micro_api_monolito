package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos

data class CreateHostDto(
    val hostId: Int = 0,
    val order: Int = 0,
    val name: String = "",
    val host: String = "",
    val status: String = ""
)