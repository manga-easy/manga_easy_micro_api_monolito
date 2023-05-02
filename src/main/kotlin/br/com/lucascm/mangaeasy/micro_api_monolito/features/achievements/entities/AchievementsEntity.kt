package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities

import jakarta.persistence.*

@Entity
@Table(name = "_1_database_1_collection_5")
class AchievementsEntity {
    @Id
    @GeneratedValue
    private val _id: Long? = null
    @Column(nullable = false)
    var _uid: String? = null
        private set
    @Column(nullable = false)
    var name: String? = null
        private set
    @Column(nullable = false)
    var rarity: String? = null
        private set
    @Column(nullable = false)
    var description: String? = null
        private set
    @Column(nullable = false)
    var percent: Float? = null
        private set
    @Column(nullable = false)
    var url: String? = null
        private set
    @Column(nullable = false)
    var adsoff: Boolean? = null
        private set
    @Column(nullable = false)
    var benefits: String? = null
        private set
    @Column(nullable = false)
    var disponivel: Boolean? = null
        private set
    @Column(nullable = false)
    var categoria: String? = null
        private set
    @Column(nullable = false)
    var type: String? = null
        private set
    @Column(nullable = false)
    var time_cria: Long? = null
        private set
    @Column(nullable = false)
    var _createdat: Long? = null
        private set
    @Column(nullable = false)
    var _updatedat: Long? = null
        private set
}