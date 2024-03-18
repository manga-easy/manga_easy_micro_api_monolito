data class MangasEntity(
    val romajiTitle: String,
    val englishTitle: String,
    val nativeTitle: String,
    val titles: List<*>
) {
    companion object {
        fun fromJson(json: Map<String, Any>): MangasEntity {
            val titlesList = (json["data"] as Map<String, Any>)["Page"] as Map<String, Any>
            val title = (titlesList["media"] as Map<String, Any>)["title"] as Map<String, Any>
            val romajiTitle = title["romaji"] as? String ?: "N/A"
            val englishTitle = title["english"] as? String ?: "N/A"
            val nativeTitle = title["native"] as? String ?: "N/A"

            return MangasEntity(romajiTitle, englishTitle, nativeTitle, titles)
        }

        fun fromApiRecommendations(json: Map<String, Any>): MangasEntity {
            val romajiTitle = json["mediaRecommendation"]?.get("title")?.get("romaji") as? String ?: ""
            val englishTitle = json["mediaRecommendation"]?.get("title")?.get("english") as? String ?: ""
            return MangasEntity(romajiTitle, englishTitle, "", emptyList())
        }
    }
}