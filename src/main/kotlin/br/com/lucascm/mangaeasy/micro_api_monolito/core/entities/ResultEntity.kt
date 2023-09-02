package br.com.lucascm.mangaeasy.micro_api_monolito.core.entities

import java.util.Objects

class ResultEntity(
    val status: StatusResultEnum,
    val message: String?,
    val total: Int,
    val data: List<Any>
)