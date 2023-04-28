package br.com.lucascm.mangaeasy.micro_api_monolito.features.chapter_notifications.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.chapter_notifications.entities.ChapterNotificationsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChapterNotificationsRepository : JpaRepository<ChapterNotificationsEntity, Long> {

    @Query("select a from ChapterNotificationsEntity a where uniqueid = ?1 and idhost = ?2 and datetime >= ?3")
    fun findAllByUniqueidAndIdhostAndDatetime(
        uniqueid: String,
        idhost: Int,
        datetime: Long
    ): List<ChapterNotificationsEntity>
}