package br.com.lucascm.mangaeasy.micro_api_monolito.features.users.entities

import jakarta.persistence.*


@Entity
@Table(name = "_1_database_1_collection_4")
data class UsersAchievementsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private val id: Long? = null,

    @Column(name = "_uid")
    val uid: String? = null,
    val idemblema: String = "",
    val timecria: Long? = null,
    val userid: String = "",

    @Column(name = "_createdat")
    val createdat: Long? = null,

    @Column(name = "_updatedat")
    val updatedat: Long? = null,
)