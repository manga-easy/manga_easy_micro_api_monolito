package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities
import jakarta.persistence.*

@Entity
@Table(name = "_1_database_1_collection_6")
data class HistoriesEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "_id")
        private val id: Long? = null,
        @Column(name = "_uid")
        val uid: String? = null,
        val uniqueid: String = "",
        val manga: String = "",
        val iduser: String = "",
        @Deprecated(message = "User currentChapter 0.13.0 -> 0.15.0")
        val capatual: String? = null,
        var currentchapter: String? = null,
        val updatedat: Long? = null,
        val createdat: Long? = null,
        val isdeleted: Boolean = false,
        val chapterlidos: String? = null
)