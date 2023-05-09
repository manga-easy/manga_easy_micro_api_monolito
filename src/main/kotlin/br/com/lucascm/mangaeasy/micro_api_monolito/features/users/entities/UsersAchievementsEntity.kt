package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities

import jakarta.persistence.*


@Entity
@Table(name = "_1_database_1_collection_4")
class UsersAchievementsEntity
 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private var id: Long? = null

    @Column(name = "_uid")
    var uid: String? = null
    var idemblema: String? = null
    var timecria: Long? = null
    var userid: String? = null

    @Column(name = "_createdat")
    var createdat: Long? = null

    @Column(name = "_updatedat")
    var updatedat: Long? = null
}