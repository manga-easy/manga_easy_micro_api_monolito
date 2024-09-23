package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostMangaSearchEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.MangaEntity

class HostMangaSearchDto(
    val search: String,
    val versionApp: String,
    val data: List<MangaEntity>,
) {
    fun toEntity(hostId: Int): HostMangaSearchEntity {
        return HostMangaSearchEntity(
            data = data,
            id = "$hostId<>$search",
        )
    }
}