package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "banner")
data class BannersEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    val image: String = "",
    val link: String = "",

    @Column(name = "createdat")
    val createdat: Long = 0,

    @Column(name = "updatedat")
    val updatedat: Long = 0,
)