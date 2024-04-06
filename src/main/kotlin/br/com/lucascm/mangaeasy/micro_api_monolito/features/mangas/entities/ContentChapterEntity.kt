package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.*
import java.util.concurrent.TimeUnit

@RedisHash("content-chapter")
data class ContentChapterEntity(
    @Id
    val id: String? = null,
    val idhost: Int,
    val uniqueid: String,
    val chapter: String,
    val versionApp: String = "0.14.0",
    val data: List<ImageChapterEntity>,
    val creatAt: Date?,
    @TimeToLive(unit = TimeUnit.DAYS)
    var time: Long = 30
) {
    fun getCustom(): String {
        return "$idhost<>$uniqueid<>$chapter<>$versionApp"
    }
}