package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities

data class ChapterEntity(
    val id: String?,
    val title: String,
    val number: Double?,
    val href: String?,
    val date: String?,
    val imagens: List<ImageChapterEntity> = listOf(),
    val description: String?,
    val scan: String?
)