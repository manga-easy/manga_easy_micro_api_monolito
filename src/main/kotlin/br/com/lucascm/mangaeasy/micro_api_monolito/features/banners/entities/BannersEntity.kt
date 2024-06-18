package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.entities

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "_1_database_1_collection_9")
data class BannersEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private var id: Long? = null,
    @UuidGenerator
    @Column(name = "_uid")
    var uid: String = "",
    var image: String = "",
    var link: String = "",

    @Column(name = "_createdat")
    var createdat: Long? = null,

    @Column(name = "_updatedat")
    var updatedat: Long? = null,
)