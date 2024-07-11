package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.MangaEntity

data class FavoriteManga(
    val manga: MangaEntity? = null,
    val order: Int = 0,
)