package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.DetailsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.LangManga
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.MangaDetailsEntity

class HostMangaDto(
    val versionApp: String,
    val data: DetailsEntity,
    val langManga: LangManga?,
) {
    fun toEntity(hostId: Int, uniqueId: String): MangaDetailsEntity {
        return MangaDetailsEntity(
            id = MangaDetailsEntity.getId(
                hostId = hostId,
                uniqueId = uniqueId,
                langManga = langManga
            ),
            data = data
        )
    }
}