package br.com.lucascm.mangaeasy.micro_api_monolito.features.chapter_notifications.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable
@Entity
@Table(name = "_1_database_1_collection_13")
class ChapterNotificationsEntity: Serializable {

    @Id
    @GeneratedValue
    private val _id: Long? = null

    @Column(nullable = false)
    var _uid: String? = null
        private set
    @Column(nullable = false)
    var uniqueid: String? = null
        private set
    @Column(nullable = false)
    var capitulos: String? = null
        private set
    @Column(nullable = false)
    var nomemanga: String? = null
        private set
    @Column(nullable = false)
    var idhost: Int? = null
        private set
    @Column(nullable = false)
    var datetime: Long? = null
        private set
    @Column(nullable = false)
    var identific: String? = null
        private set
}