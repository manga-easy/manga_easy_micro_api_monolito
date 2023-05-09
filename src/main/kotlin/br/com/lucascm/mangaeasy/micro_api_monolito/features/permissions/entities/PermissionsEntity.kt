package br.com.lucascm.mangaeasy.micro_api_monolito.features.permissions.entities

import jakarta.persistence.*
@Entity
@Table(name = "_1_database_1_collection_11")
class PermissionsEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: Long? = null

    @Column(name = "_uid")
    var uid: String? = null
    var userid: String? = null
    var value: Int? = null

    @Column(name = "_createdat")
    var createdat: Long? = null

    @Column(name = "_updatedat")
    var updatedat: Long? = null
}