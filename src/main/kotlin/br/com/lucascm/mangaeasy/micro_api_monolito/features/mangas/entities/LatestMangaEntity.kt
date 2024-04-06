package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.*
import java.util.concurrent.TimeUnit

@RedisHash("latest-manga")
data class LatestMangaEntity(
    @Id
    val id: String? = null,
    val idhost: Int,
    val versionApp: String = "0.14.0",
    val data: List<MangaEntity>,
    val creatAt: Date?,
    @TimeToLive(unit = TimeUnit.HOURS)
    var time: Long = 12
) {
    fun getCustom(): String {
        return "$idhost<>$versionApp"
    }
}