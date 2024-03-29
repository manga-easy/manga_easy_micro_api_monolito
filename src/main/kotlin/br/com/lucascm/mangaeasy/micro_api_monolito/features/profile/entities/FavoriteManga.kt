package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.MangaEntity

data class FavoriteManga(
    val manga: MangaEntity,
    val order: Int = 0,
)
