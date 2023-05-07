package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities

import jakarta.persistence.*
@Entity
@Table(name = "_1_database_1_collection_10")
class NotificationsEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: Long? = null

    @Column(name = "_uid")
    var uid: String? = null
    var titulo: String? = null
    var menssege: String? = null
    var image: String? = null
    var datemade: Long? = null
    @Column(name = "_createdat")
    var createdat: Long? = null

    @Column(name = "_updatedat")
    var updatedat: Long? = null
}