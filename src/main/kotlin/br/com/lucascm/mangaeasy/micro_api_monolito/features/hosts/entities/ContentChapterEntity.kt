package br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("content-chapter")
data class ContentChapterEntity(
    @Id
    val id: String? = null,
    val data: List<ImageChapterEntity>,
    @TimeToLive(unit = TimeUnit.DAYS)
    var time: Long = 30,
) {
    companion object {
        fun getId(hostId: Int, uniqueId: String, chapterId: String, langManga: LangManga?): String {
            var id = "$hostId<>$uniqueId<>$chapterId"
            if (langManga != null) {
                id += "<>$langManga"
            }
            return id
        }
    }
}