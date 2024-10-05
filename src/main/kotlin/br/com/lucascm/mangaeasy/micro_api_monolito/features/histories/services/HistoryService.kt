package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.services

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoryEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.UpdateHistoryDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.repositories.HistoriesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class HistoryService {
    @Autowired
    lateinit var historiesRepository: HistoriesRepository
    fun findByUserIdAndUniqueId(userId: String, uniqueId: String): HistoryEntity? {
        val result = historiesRepository.findByUserIdAndUniqueIdOrderByUpdatedAtDesc(userId, uniqueId)
        if (result.get().isEmpty()) {
            return null
        }
        val histories = result.get()
        if (histories.size == 1) return histories.first()
        val history = histories.first()
        for (item in histories.drop(1)) {
            historiesRepository.deleteById(item.id!!)
        }
        return history
    }

    fun findByUserId(userId: String, pageable: Pageable): List<HistoryEntity> {
        return historiesRepository.findByUserId(userId, pageable)
    }

    fun update(historyId: String, body: UpdateHistoryDto): HistoryEntity {
        if (body.uniqueId.isEmpty()) {
            throw BusinessException("UniqueId não pode ser vazio")
        }
        val result = historiesRepository.findById(historyId).getOrNull()
            ?: throw BusinessException("Manga não encontrado")
        return historiesRepository.save(
            result.copy(
                updatedAt = Date().time,
                chaptersRead = body.chaptersRead,
                currentChapter = body.currentChapter,
                isDeleted = body.hasDeleted,
                manga = body.manga,
                uniqueId = body.uniqueId
            )
        )
    }

    fun create(userId: String, body: UpdateHistoryDto): HistoryEntity {
        if (body.uniqueId.isEmpty()) {
            throw BusinessException("UniqueId não pode ser vazio")
        }
        val result = historiesRepository.findByUserIdAndUniqueIdOrderByUpdatedAtDesc(
            userId = userId,
            uniqueId = body.uniqueId
        )
        if (!result.isEmpty) {
            throw BusinessException("Manga já cadastrado")
        }
        return historiesRepository.save(
            HistoryEntity(
                updatedAt = Date().time,
                createdAt = Date().time,
                userId = userId,
                uniqueId = body.uniqueId,
                chaptersRead = body.chaptersRead,
                currentChapter = body.currentChapter,
                isDeleted = body.hasDeleted,
                manga = body.manga,
                catalogId = null,
            )
        )
    }
}