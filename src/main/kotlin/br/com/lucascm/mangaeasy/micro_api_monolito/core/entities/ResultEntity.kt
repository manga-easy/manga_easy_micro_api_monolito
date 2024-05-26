package br.com.lucascm.mangaeasy.micro_api_monolito.core.entities

@Deprecated(message = "Descontinuado")
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