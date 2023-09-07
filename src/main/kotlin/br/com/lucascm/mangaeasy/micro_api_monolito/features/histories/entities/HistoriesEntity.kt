package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.ChapterEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.MangaEntity
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "_1_database_1_collection_6")
data class HistoriesEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "_id")
        private val id: Long? = null,

        @Column(name = "_uid")
        val uid: String? = null,

        val uniqueid: String,
        val manga: MangaEntity,
        val idUser: String,
        @Deprecated(message = "User currentChapter 0.13.0 -> 0.15.0")
        val capAtual: ChapterEntity?,
        var currentChapter: String?,
        val updatedAt: Int,
        val createdAt: Int,
        val isDeleted: Boolean,
        val chapterLidos: List<String>,
        val isSync: Boolean
)