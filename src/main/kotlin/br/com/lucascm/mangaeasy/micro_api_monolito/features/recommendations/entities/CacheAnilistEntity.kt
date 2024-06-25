import com.fasterxml.jackson.annotation.JsonProperty

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
    @JsonProperty("bannerImage") val bannerImage: String?
)

data class Title(
    @JsonProperty("english") val english: String?,
    @JsonProperty("romaji") val romaji: String?
)