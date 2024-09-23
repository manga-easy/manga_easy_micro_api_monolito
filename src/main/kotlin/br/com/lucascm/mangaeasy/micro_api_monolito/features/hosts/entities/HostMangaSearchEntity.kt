package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("host-manga-search")
data class HostMangaSearchEntity(
    @Id
    val id: String? = null,
    val data: List<MangaEntity>,
    @TimeToLive(unit = TimeUnit.DAYS)
    var time: Long = 3,
)