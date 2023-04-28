package br.com.lucascm.mangaeasy.micro_api_monolito.core.entities

class ResultEntity<T>(
    val status: StatusResultEnum,
    val message: String?,
    val total : Int,
    val data : List<T>
)