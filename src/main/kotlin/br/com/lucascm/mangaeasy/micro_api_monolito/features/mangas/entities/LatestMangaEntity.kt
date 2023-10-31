package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

import jakarta.persistence.*
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "latest-manga")
data class LatestMangaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: ObjectId?,
    val idhost: Int,
    val data: List<MangaEntity>,
    val creatAt: Date?,
    val versionApp: String?,
)