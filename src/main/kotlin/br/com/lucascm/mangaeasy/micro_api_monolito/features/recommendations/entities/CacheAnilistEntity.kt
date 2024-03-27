import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

data class MediaEntity(
    @JsonProperty("data") val data: Data
)

data class Data(
    @JsonProperty("Media") val media: Media
)

data class Media(
    @JsonProperty("recommendations") val recommendations: Recommendations
)

data class Recommendations(
    @JsonProperty("nodes") val nodes: List<Node>
)

data class Node(
    @JsonProperty("mediaRecommendation") val mediaRecommendation: MediaRecommendation
)

data class MediaRecommendation(
    @JsonProperty("title") val title: Title,
    @JsonProperty("coverImage") val coverImage: CoverImage,
    @JsonProperty("bannerImage") val bannerImage: String?
)

data class Title(
    @JsonProperty("english") val english: String?,
    @JsonProperty("romaji") val romaji: String?
)

data class CoverImage(
    @JsonProperty("extraLarge") val extraLarge: String?,
)

data class AnilistRecommendationEntity(
    var english: String?,
    var romanji: String?,
    var bannerImage: String?,
    var coverImage: String?,
)

@RedisHash("anilist-recommendation")
data class CacheAnilistEntity(
    @Id
    var id: String? = null,
    var title: String = "",
    var recommendation: List<RecommendationsEntity>,
    @TimeToLive(unit = TimeUnit.DAYS)
    var time: Long = 30
)