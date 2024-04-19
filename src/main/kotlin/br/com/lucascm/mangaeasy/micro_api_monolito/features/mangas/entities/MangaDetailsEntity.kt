package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("manga-details")
data class MangaDetailsEntity(
    @Id
    val id: String?,
    val data: DetailsEntity,
    @TimeToLive(unit = TimeUnit.DAYS)
    var time: Long = 1,
)