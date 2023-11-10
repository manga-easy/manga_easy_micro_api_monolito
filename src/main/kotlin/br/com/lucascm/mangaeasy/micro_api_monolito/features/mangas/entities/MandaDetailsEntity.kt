package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
@Document(collection = "manga-details")
data class MandaDetailsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: ObjectId?,
    val idhost: Int,
    val uniqueid: String,
    val data: DetailsEntity,
    @Column(name = "version_app")
    val versionApp: String?,
    val creatAt: Date?
)