package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities

import jakarta.persistence.*
@Entity
@Table(name = "_1_database_1_collection_7")
class UsersLevelsEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private var id: Long? = null

    @Column(name = "_uid")
    var uid: String? = null
    var name: String? = null
    var total: Int? = null
    var temporada: String? = null
    var lvl: Long? = null
    var timecria: Long? = null
    var quantity: Long? = null
    var minute: Long? = null
    var userid: String? = null
    var timeup: Long? = null

    @Column(name = "_createdat")
    var createdat: Long? = null

    @Column(name = "_updatedat")
    var updatedat: Long? = null
}