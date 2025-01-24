package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.ContentChapterEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.ImageChapterEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.LangManga

class HostMangaChapterDto(
    val versionApp: String,
    val data: List<ImageChapterEntity>,
    val langManga: LangManga?
) {
    fun toEntity(hostId: Int, uniqueId: String, chapterId: String): ContentChapterEntity {
        return ContentChapterEntity(
            data = data,
            id = ContentChapterEntity.getId(
                hostId,
                uniqueId,
                chapterId,
                langManga
            ),
        )
    }
}