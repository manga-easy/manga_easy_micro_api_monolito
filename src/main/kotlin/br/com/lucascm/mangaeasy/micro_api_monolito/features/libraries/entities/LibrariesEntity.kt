package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities

import jakarta.persistence.*

@Entity
@Table(name = "_1_database_1_collection_1")
data class LibrariesEntity (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "_id")
        private val id: Long? = null,
        @Column(name = "_uid")
        val uid: String?,
        val idhost: Long?,
        val idmanga: String?,
        val iduser: String,
        val uniqueid: String,
        val manga: String?,
        val status: String?,
        val updatedat: Long?,
        val createdat: Long?,
        val isdeleted: Boolean
)