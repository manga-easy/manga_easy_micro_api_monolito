package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable
@Entity
@Table(name = "_1_database_1_collection_3")
class HostsEntity: Serializable {

    @Id
    @GeneratedValue
    private val _id: Long? = null

    @Column(nullable = false)
    var _uid: String? = null
        private set
    @Column(nullable = false)
    var idhost: Int? = null
        private set
    @Column(nullable = false)
    var order: Long? = null
        private set
    @Column(nullable = false)
    var name: String? = null
        private set
    @Column(nullable = false)
    var host: String? = null
        private set
    @Column(nullable = false)
    var status: String? = null
        private set
    @Column(nullable = false)
    var interstitialadunitid: String? = null
        private set
    @Column(nullable = false)
    var _createdat: Long? = null
        private set
    @Column(nullable = false)
    var _updatedat: Long? = null
        private set
}