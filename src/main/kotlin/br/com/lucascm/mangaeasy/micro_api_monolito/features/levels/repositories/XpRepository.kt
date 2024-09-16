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
}