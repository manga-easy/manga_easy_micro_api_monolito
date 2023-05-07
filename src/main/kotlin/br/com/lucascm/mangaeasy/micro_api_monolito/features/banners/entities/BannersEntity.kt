package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.entities

import jakarta.persistence.*
@Entity
@Table(name = "_1_database_1_collection_9")
class BannersEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: Long? = null

    @Column(name = "_uid")
    var uid: String? = null
    var image: String? = null
    var link: String? = null
    var `type`: String? = null

    @Column(name = "_createdat")
    var createdat: Long? = null

    @Column(name = "_updatedat")
    var updatedat: Long? = null
}