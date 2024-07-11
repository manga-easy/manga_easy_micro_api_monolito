package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities

data class UpdateHistoryDto(
    val uniqueid: String = "",
    val manga: String = "",
    var currentChapter: String? = null,
    val hasDeleted: Boolean = false,
    val chaptersRead: String = ""
)