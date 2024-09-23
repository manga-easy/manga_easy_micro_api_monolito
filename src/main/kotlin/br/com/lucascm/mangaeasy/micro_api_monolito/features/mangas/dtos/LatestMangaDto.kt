package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.LatestMangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.MangaEntity

@Deprecated("Remover 0.18 -> 0.20")
class LatestMangaDto(
    val idhost: Int,
    val versionApp: String,
    val data: List<MangaEntity>,
) {
    fun toEntity(): LatestMangaEntity {
        return LatestMangaEntity(
            data = data,
            id = "$idhost",
        )
    }
}