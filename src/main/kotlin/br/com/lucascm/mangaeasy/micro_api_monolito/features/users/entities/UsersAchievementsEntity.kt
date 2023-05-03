package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities

import jakarta.persistence.*

@Entity
@Table(name = "_1_database_1_collection_4")
class UsersAchievementsEntity {
    @Id
    @GeneratedValue
    private val _id: Long? = null
    @Column(nullable = false)
    var _uid: String? = null
        private set
    @Column(nullable = false)
    var idemblema: String? = null
        private set
    @Column(nullable = false)
    var timecria: Long? = null
        private set
    @Column(nullable = false)
    var userid: String? = null
        private set
    @Column(nullable = false)
    var _createdat: Long? = null
        private set
    @Column(nullable = false)
    var _updatedat: Long? = null
        private set
}