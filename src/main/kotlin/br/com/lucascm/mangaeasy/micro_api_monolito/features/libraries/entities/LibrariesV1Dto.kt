package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities

@Deprecated("Remover 0.18 -> 0.20")
data class LibrariesV1Dto(
    val uid: String? = null,
    val idhost: Long? = null,
    var iduser: String = "",
    val uniqueid: String = "",
    val manga: String? = null,
    val status: String? = null,
    val updatedat: Long? = null,
    val createdat: Long? = null,
    val isdeleted: Boolean = false
) {
    companion object {
        fun fromEntity(entity: LibrariesEntity): LibrariesV1Dto {
            return LibrariesV1Dto(
                uid = entity.id,
                idhost = entity.hostId,
                iduser = entity.userId,
                uniqueid = entity.uniqueid,
                manga = entity.manga,
                status = entity.status,
                updatedat = entity.updatedAt,
                createdat = entity.createdAt,
                isdeleted = entity.hasDeleted
            )
        }
    }
}