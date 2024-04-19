package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.DetailsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.MangaDetailsEntity

class MangaDetailsDto(
    val idhost: Int,
    val uniqueid: String,
    val versionApp: String = "0.14.0",
    val data: DetailsEntity,
) {
    fun toEntity(): MangaDetailsEntity {
        return MangaDetailsEntity(
            id = "$idhost<>$uniqueid",
            data = data
        )
    }
}