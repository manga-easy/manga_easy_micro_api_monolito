package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.*
import java.util.concurrent.TimeUnit

@RedisHash("manga-details")
data class MandaDetailsEntity(
    @Id
    val id: String?,
    val idhost: Int,
    val uniqueid: String,
    val versionApp: String,
    val data: DetailsEntity,
    val creatAt: Date?,
    @TimeToLive(unit = TimeUnit.DAYS)
    var time: Long = 1,
) {
    fun getCustom(): String {
        return "$idhost<>$uniqueid<>$versionApp"
    }
}