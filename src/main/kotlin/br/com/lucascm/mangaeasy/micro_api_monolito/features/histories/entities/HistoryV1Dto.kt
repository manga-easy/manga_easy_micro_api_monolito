package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities

@Deprecated("Remover 0.18 -> 0.20")
data class HistoryV1Dto(
    val uid: String? = null,
    val uniqueid: String = "",
    val manga: String = "",
    val iduser: String = "",
    var currentchapter: String? = null,
    val updatedat: Long? = null,
    val createdat: Long? = null,
    val isdeleted: Boolean = false,
    val chapterlidos: String? = null
) {
    companion object {
        fun fromEntity(entity: HistoryEntity): HistoryV1Dto {
            return HistoryV1Dto(
                uid = entity.id,
                uniqueid = entity.uniqueId,
                manga = entity.manga,
                iduser = entity.userId,
                currentchapter = entity.currentChapter,
                updatedat = entity.updatedAt,
                createdat = entity.createdAt,
                isdeleted = entity.isDeleted,
                chapterlidos = entity.chaptersRead
            )
        }
    }
}