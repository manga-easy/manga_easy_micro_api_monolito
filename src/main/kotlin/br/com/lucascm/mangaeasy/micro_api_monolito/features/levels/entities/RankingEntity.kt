package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("ranking")
class RankingEntity (
    @Id
    var id: String? = null,//É o userId
    var totalXp: Long = 0,
    var place: Long = 0,
    var picture: String? = null,
    var name: String? = null,
    @TimeToLive(unit = TimeUnit.DAYS)
    var time: Long = 3
)