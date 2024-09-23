package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.dtos

import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.ContentChapterEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.ImageChapterEntity

@Deprecated("Remover 0.18 -> 0.20")
class ContentChapterDto(
    val idhost: Int,
    val uniqueid: String,
    val chapter: String,
    val versionApp: String,
    val data: List<ImageChapterEntity>,
) {
    fun toEntity(): ContentChapterEntity {
        return ContentChapterEntity(
            data = data,
            id = "$idhost<>$uniqueid<>$chapter"
        )
    }
}