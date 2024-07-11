package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities

data class MangaEntity(
    val uniqueid: String,
    val title: String,
    val href: String,
    val capa: String,
    val idHost: Int
)