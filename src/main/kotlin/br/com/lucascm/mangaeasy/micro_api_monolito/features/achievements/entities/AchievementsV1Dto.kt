package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities


data class AchievementsV1Dto(
    var uid: String? = null,
    var name: String = "",
    var rarity: String = "",
    var description: String = "",
    var percent: Double = 0.0,
    var url: String = "",
    var adsoff: Boolean = false,
    var benefits: String = "",
    var disponivel: Boolean = false,
    var categoria: String = "",
    var type: String = "",
    var time_cria: Long = 0,
    var createdat: Long? = null,
    var updatedat: Long? = null
) {
    companion object {
        fun fromEntity(entity: AchievementsEntity): AchievementsV1Dto {
            return AchievementsV1Dto(
                uid = entity.id,
                name = entity.name,
                rarity = entity.rarity,
                description = entity.description,
                percent = entity.percentRarity,
                url = entity.url,
                benefits = entity.benefits,
                disponivel = entity.reclaim,
                categoria = entity.category,
                time_cria = entity.totalAcquired,
                createdat = entity.createdAt,
                updatedat = entity.updatedAt,
                adsoff = false,
                type = "link"
            )
        }
    }
}