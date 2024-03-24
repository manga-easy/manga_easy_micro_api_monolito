package br.com.lucascm.mangaeasy.micro_api_monolito.core.entities

class ResultEntity(
    val status: StatusResultEnum,
    val message: String?,
    val total: Int,
    val data: List<Any>
) {
    constructor (data: List<Any>) : this(
        StatusResultEnum.SUCCESS,
        "Sucesso",
        data.size,
        data
    )
}