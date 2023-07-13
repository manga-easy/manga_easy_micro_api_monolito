package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities

import jakarta.persistence.*

@Entity
@Table(name = "_1_database_1_collection_5")
data class AchievementsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private val id: Long? = null,
    @Column(name = "_uid")
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
    @Column(name = "_createdat")
    var createdat: Long? = null,
    @Column(name = "_updatedat")
    var updatedat: Long? = null,
)