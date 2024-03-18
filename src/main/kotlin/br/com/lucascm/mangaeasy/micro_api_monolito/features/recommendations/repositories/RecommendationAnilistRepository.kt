package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities.RecommendationsEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class RecommendationAnilistRepository{
    val client = RestTemplate()

    val query ="""
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
    fun getRecommendationByTitle(title: String): List<RecommendationsEntity> {

       var result = client.postForEntity(urlAnilist, mapOf("query" to query.replace(":title", title)), Any::class.java)
        return emptyList()
    }
}