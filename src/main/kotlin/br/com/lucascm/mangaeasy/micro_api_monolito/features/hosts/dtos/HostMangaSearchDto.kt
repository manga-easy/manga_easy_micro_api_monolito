package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.HostMangaSearchEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.LangManga
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.MangaEntity

class HostMangaSearchDto(
    val search: String,
    val versionApp: String,
    val data: List<MangaEntity>,
    val langManga: LangManga?,
) {
    fun toEntity(hostId: Int): HostMangaSearchEntity {
        return HostMangaSearchEntity(
            data = data,
            id = HostMangaSearchEntity.getId(hostId, search, langManga),
        )
    }
}