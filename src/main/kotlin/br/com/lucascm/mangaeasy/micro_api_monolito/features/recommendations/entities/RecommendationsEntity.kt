package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities

import jakarta.persistence.*

@Entity
@Table(name = "_1_database_1_collection_14")
data class RecommendationsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private var id: Long? = null,
    @Column(name = "_uid")
    var uid: String? = null,
    var uniqueid: String = "",
    var title: String = "",
    var link: String = "",
    var artistid: String? = null,
    var artistname: String? = null,
    var datacria: Long? = null,
    @Column(name = "_createdat")
    var createdat: Long? = null,
    @Column(name = "_updatedat")
    var updatedat: Long? = null,
    var image: String? = null,
)