package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "history")
data class HistoriesEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    val uniqueid: String = "",
    val manga: String = "",

    @Column(name = "user_id", nullable = false)
    val userId: String = "",

    @Column(name = "current_chapter")
    var currentChapter: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Long = 0,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Long = 0,

    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean = false,

    @Column(name = "chapters_read", nullable = false)
    val chaptersRead: String = ""
)