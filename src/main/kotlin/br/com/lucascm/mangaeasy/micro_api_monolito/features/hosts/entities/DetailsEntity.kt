package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities

data class DetailsEntity(
    val id: String?,
    val title: String,
    val uniqueid: String,
    val capa: String,
    val sinopse: String,
    val generos: List<GenderEntity> = listOf(),
    val autor: String,
    val artista: String,
    val capitulos: List<ChapterEntity> = listOf(),
    val ano: String,
    val scans: String,
    val status: String,
    val idHost: Int
)