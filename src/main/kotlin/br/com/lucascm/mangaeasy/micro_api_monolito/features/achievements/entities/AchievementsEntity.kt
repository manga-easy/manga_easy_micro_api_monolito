package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities

import jakarta.persistence.*

@Entity
@Table(name = "_1_database_1_collection_5")
class AchievementsEntity {
    @Id
    @GeneratedValue
    @Column(name = "_id")
    private val id: Long? = null
    @Column(name = "_uid")
    var uid: String? = null
    var name: String? = null
    var rarity: String? = null

    var description: String? = null
    var percent: Float? = null
    var url: String? = null
    var adsoff: Boolean? = null
    var benefits: String? = null
    var disponivel: Boolean? = null
    var categoria: String? = null
    var type: String? = null
    var time_cria: Long? = null
        private set
    @Column(name = "_createdat")
    var createdat: Long? = null
    @Column(name = "_updatedat")
    var updatedat: Long? = null
}