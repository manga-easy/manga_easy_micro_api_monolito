package br.com.lucascm.mangaeasy.micro_api_monolito.core.entities

class BusinessException(
    override val message: String,
    val code: BusinessCode? = null
) : Exception() {
}