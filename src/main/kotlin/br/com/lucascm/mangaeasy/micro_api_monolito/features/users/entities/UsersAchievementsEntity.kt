package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities

import jakarta.persistence.*


@Entity
@Table(name = "_1_database_1_collection_4")
class UsersAchievementsEntity
 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var _id: Long? = null
    var _uid: String? = null
    var idemblema: String? = null
    var timecria: Long? = null
    var userid: String? = null
    var _createdat: Long? = null
    var _updatedat: Long? = null
}