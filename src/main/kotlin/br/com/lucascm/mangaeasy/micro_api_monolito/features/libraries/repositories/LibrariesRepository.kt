package br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.libraries.entities.LibrariesEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LibrariesRepository : JpaRepository<LibrariesEntity, String> {

    fun findByUserIdAndUniqueIdOrderByUpdatedAtDesc(userId: String, uniqueId: String): Optional<List<LibrariesEntity>>

    fun findByUserId(userId: String, pageable: Pageable): List<LibrariesEntity>

    @Query(
        """
        SELECT COUNT(*)
        FROM LibrariesEntity dc 
        WHERE dc.status = 'lido' 
        AND dc.userId = :userId
    """
    )
    fun countByStatusAndUserId(@Param("userId") userId: String): Long
}