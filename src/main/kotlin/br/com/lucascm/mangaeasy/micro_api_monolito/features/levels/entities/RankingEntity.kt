package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("ranking")
class RankingEntity (
    @Id
    var id: String? = null,
    var userId: String,
    var totalXp: Long,
    var place: Long,
    var picture: String?,
    var name: String?,
    @TimeToLive(unit = TimeUnit.DAYS)
    var time: Long = 3
)