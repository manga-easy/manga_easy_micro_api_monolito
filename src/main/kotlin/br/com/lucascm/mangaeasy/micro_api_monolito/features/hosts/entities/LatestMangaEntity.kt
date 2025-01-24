package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("latest-manga")
data class LatestMangaEntity(
    @Id
    val id: String? = null,
    val data: List<MangaEntity>,
    @TimeToLive(unit = TimeUnit.HOURS)
    var time: Long = 12,
) {
    companion object {
        fun getId(hostId: Int, langManga: LangManga?): String {
            var id = "$hostId"
            if (langManga != null) {
                id += "<>$langManga"
            }
            return id
        }
    }
}