package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

data class ImageChapterEntity(
    val id: String?,
    val src: String,
    val path: String?,
    val tipo: TypeFontEnum
)