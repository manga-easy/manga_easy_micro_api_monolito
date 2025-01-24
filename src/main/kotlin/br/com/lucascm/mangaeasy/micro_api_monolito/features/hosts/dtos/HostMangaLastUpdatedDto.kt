package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.LangManga
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.LatestMangaEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.MangaEntity

class HostMangaLastUpdatedDto(
    val versionApp: String,
    val data: List<MangaEntity>,
    val langManga: LangManga?,
) {
    fun toEntity(hostId: Int): LatestMangaEntity {
        return LatestMangaEntity(
            data = data,
            id = LatestMangaEntity.getId(hostId, langManga),
        )
    }
}