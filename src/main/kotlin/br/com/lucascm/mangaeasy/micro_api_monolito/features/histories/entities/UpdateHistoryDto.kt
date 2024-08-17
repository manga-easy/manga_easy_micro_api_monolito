package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities

data class UpdateHistoryDto(
    val uniqueId: String = "",
    val manga: String = "",
    var currentChapter: String? = null,
    val hasDeleted: Boolean = false,
    val chaptersRead: String = ""
)