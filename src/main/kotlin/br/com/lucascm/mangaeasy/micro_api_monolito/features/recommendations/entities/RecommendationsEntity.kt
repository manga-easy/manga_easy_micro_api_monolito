package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities

import jakarta.persistence.*
@Entity
@Table(name = "_1_database_1_collection_14")
class RecommendationsEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: Long? = null
    var _uid: String? = null
    var uniqueid: String? = null
    var title: String? = null
    var link: String? = null
    var datacria: Long? = null
    var _createdat: Long? = null
    var _updatedat: Long? = null
}