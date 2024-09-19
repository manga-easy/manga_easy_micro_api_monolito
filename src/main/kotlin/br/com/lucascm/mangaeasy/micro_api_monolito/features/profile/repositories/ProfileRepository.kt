package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository : JpaRepository<ProfileEntity, String> {
    fun findByUserId(userId: String): ProfileEntity?

    @Query(
        """
        select user_id, total_xp, name, picture from profile p 
        WHERE total_xp <> 0 
        ORDER by total_xp DESC
        limit 100
        OFFSET :offset
        """,
        nativeQuery = true,
    )
    fun countXpRanking(offset: Long): List<Map<String, Any>>
}