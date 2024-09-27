package br.com.lucascm.mangaeasy.micro_api_monolito.core.entities

class RedisCacheName {
    companion object {
        const val LIST_CATALOG = "list-catalog"
        const val LIST_REVIEW = "list-review"
        const val GET_MANGA_WEEKLY = "most-manga-weekly"
        const val RECOMMENDATIONS_ANILIST = "recommendations-anilist"
        const val RECOMMENDATIONS = "recommendations"
        const val LIST_REVIEW_LAST = "list-review-last"
        const val REVIEW_RATING_STATISTICS = "review-rating-statistics"
        const val PROFILE = "profile"
        const val CATALOG = "catalog"
        const val TOGGLE = "toggle"
    }

}