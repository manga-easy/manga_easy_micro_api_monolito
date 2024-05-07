package br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.levels.entities.XpEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface XpRepository : JpaRepository<XpEntity, Long> {
    @Query(
        """
        SELECT sum(quantity)
        FROM XpEntity
        WHERE uniqueID = :uniqueID
            AND userID = :userID
            AND chapterNumber = :chapterNumber
        """
    )
    fun countXpByUniqueidAndChapterNumber(
        @Param("userID") userID: String,
        @Param("uniqueID") uniqueID: String,
        @Param("chapterNumber") chapterNumber: String,
    ): Long?

    @Query(
        """
        SELECT sum(quantity)
        FROM XpEntity
        WHERE userID = :userID
        """
    )
    fun countXpTotalByUserId(
        @Param("userID") userID: String,
    ): Long?

    fun findByUserIDAndUniqueIDAndChapterNumber(userID: String, uniqueID: String, chapterNumber: String): List<XpEntity>

    @Query(
        """
        SELECT sum(quantity) as Total, userID as userId
        FROM XpEntity
        group by userID
        ORDER by Total desc
        LIMIT 100
        OFFSET :offset
        """
    )
    fun countXpRanking(offset: Long): List<Map<String, Any>>

    @Query(
        """
        SELECT uniqueID
            FROM XpEntity
            WHERE YEARWEEK(FROM_UNIXTIME(updatedAt / 1000), 1) = YEARWEEK(CURDATE(), 1)
            GROUP BY uniqueID
            ORDER BY sum(quantity) DESC
        LIMIT 1
        """
    )
    fun mostMangaReadWeekly(): String
}