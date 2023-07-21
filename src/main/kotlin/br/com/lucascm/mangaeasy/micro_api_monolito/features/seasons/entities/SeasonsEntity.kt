package br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.entities

import jakarta.persistence.*
@Entity
@Table(name = "_1_database_1_collection_8")
data class SeasonsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private val id: Long? = null,

    @Column(name = "_uid")
    val uid: String? = null,
    val nome: String = "",
    val number: Int = 0,
    val datainit: Long = 0,

    @Column(name = "_createdat")
    val createdat: Long? = null,

    @Column(name = "_updatedat")
    val updatedat: Long? = null,
)