package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.DetailsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.MangaDetailsEntity

class HostMangaDto(
    val versionApp: String,
    val data: DetailsEntity,
) {
    fun toEntity(hostId: Int, uniqueId: String): MangaDetailsEntity {
        return MangaDetailsEntity(
            id = "$hostId<>$uniqueId",
            data = data
        )
    }
}