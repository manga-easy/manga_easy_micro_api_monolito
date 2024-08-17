package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoryEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HistoriesRepository : JpaRepository<HistoryEntity, String> {

    fun findByUserIdAndUniqueId(userId: String, uniqueid: String): HistoryEntity?

    fun findByUserId(userId: String, pageable: Pageable): List<HistoryEntity>
}