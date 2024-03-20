import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

data class MediaEntity(
    val data: Data
) {
    constructor() : this(Data())
}

data class Data(
    val media: Media
) {
    constructor() : this(Media())
}

data class Media(
    val recommendations: Recommendations
) {
    constructor() : this(Recommendations())
}

data class Recommendations(
    val nodes: List<Node>
) {
    constructor() : this(emptyList())
}

data class Node(
    val mediaRecommendation: MediaRecommendation
) {
    constructor() : this(MediaRecommendation())
}

@RedisHash("anilist-recommendation")
data class MediaRecommendation(
    @Id var id: String? = null,
    val title: Title,
    val bannerImage: String?,
    @TimeToLive(unit = TimeUnit.DAYS) var time: Long = 1
) {
    constructor() : this(null, Title(), null)
}

data class Title(
    val english: String?, val romaji: String?
) {
    constructor() : this(null, null)
}
