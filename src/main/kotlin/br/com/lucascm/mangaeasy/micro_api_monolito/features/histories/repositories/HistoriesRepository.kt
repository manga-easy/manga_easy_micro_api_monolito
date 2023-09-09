package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.repositories
import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoriesEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HistoriesRepository : JpaRepository<HistoriesEntity, Long> {

    fun findByIduserAndUniqueid(iduser: String, uniqueid: String): List<HistoriesEntity>

    fun findByIduser(iduser: String, pageable: Pageable): List<HistoriesEntity>
}