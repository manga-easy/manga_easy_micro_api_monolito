package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.ContentChapterEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.ImageChapterEntity

class HostMangaChapterDto(
    val versionApp: String,
    val data: List<ImageChapterEntity>,
) {
    fun toEntity(hostId: Int, uniqueId: String, chapterId: String): ContentChapterEntity {
        return ContentChapterEntity(
            data = data,
            id = "$hostId<>$uniqueId<>$chapterId"
        )
    }
}