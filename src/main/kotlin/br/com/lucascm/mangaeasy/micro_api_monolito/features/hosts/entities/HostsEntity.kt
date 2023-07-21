package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities

import jakarta.persistence.*
@Entity
@Table(name = "_1_database_1_collection_3")
data class HostsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private val id: Long? = null,
    @Column(name = "_uid")
    val uid: String? = null,
    val idhost: Int = 0,
    @Column(name = "`order`")
    val order: Int = 0,
    val name: String = "",
    val host: String = "",
    val status: String = "",
    val interstitialadunitid: String = "",
    @Column(name = "_createdat")
    val createdat: Long? = null,
    @Column(name = "_updatedat")
    val updatedat: Long? = null,
)