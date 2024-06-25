package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories

import MediaEntity
import MediaRecommendation
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class RecommendationAnilistRepository {
    val client = RestTemplate()
    val urlAnilist = "https://graphql.anilist.co"
    val query = """
        {
          Media(search: ":title") {
            recommendations {
              nodes {
                mediaRecommendation {
                  title {
                    english
                    romaji
                  }
                  bannerImage
                }
              }
            }
          }
        }
        """

    fun getRecommendationByTitle(title: String): List<MediaRecommendation> {
        return try {
            val result =
                client.postForEntity(
                    urlAnilist,
                    mapOf("query" to query.replace(":title", title)),
                    MediaEntity::class.java
                )
            result.body?.data?.media?.recommendations?.nodes?.map { it.mediaRecommendation } ?: listOf()
        } catch (e: Exception) {
            listOf()
        }
    }
}