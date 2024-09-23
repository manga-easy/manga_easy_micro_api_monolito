package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.LatestMangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.MangaEntity

class HostMangaLastUpdatedDto(
    val versionApp: String,
    val data: List<MangaEntity>,
) {
    fun toEntity(hostId: Int): LatestMangaEntity {
        return LatestMangaEntity(
            data = data,
            id = "$hostId",
        )
    }
}