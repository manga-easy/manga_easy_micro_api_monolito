package br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.entities

import jakarta.persistence.*
@Entity
@Table(name = "_1_database_1_collection_8")
class SeasonsEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: Long? = null

    @Column(name = "_uid")
    var uid: String? = null
    var nome: String? = null
    var number: Int? = null

    @Column(name = "_createdat")
    var createdat: Long? = null

    @Column(name = "_updatedat")
    var updatedat: Long? = null
}