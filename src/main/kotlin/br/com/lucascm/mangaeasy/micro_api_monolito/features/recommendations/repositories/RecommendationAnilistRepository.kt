package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories

import MediaEntity
import MediaRecommendation
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class RecommendationAnilistRepository {
    val client = RestTemplate()

    val query = """
        query{
            Media(search: ":title"){
            recommendations{
                nodes{
                    mediaRecommendation{
                        title{
                            english
                            romaji
                        }
                    }
                }
            }
        }
        }
        """
    val urlAnilist = "https://graphql.anilist.co"
    fun getRecommendationByTitle(title: String): List<MediaRecommendation> {
        val result =
            client.postForEntity(urlAnilist, mapOf("query" to query.replace(":title", title)), MediaEntity::class.java)
        return result.body?.data?.media?.recommendations?.nodes?.map { it.mediaRecommendation } ?: listOf()
    }
}