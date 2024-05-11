package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

import jakarta.persistence.*

@Entity
@Table(name = "like-manga")
data class LikeMangaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val uniqueid: String = "",
    @Column(name = "user_id", nullable = false)
    val userId: String = "",
    @Column(name = "created_at")
    val createdAt: Long? = 0,
)