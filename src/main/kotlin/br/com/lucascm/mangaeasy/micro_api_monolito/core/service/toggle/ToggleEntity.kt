package br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle

data class ResponseData(
    val status: String = "",
    val message: String = "",
    val total: Int = 0,
    val data: List<ToggleEntity> = emptyList()
)

data class ToggleEntity(
    val id: String = "",
    val name: String = "",
    val updatedAt: Long = 0,
    val status: String = "",
    val description: String = "",
    val value: Any? = null
)