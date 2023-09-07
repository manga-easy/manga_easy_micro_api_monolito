import br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities.HistoriesEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface HistoriesRepository : JpaRepository<HistoriesEntity?, Long?> {
    @Query("SELECT h FROM HistoriesEntity h WHERE h.idUser = :idUser " +
            "AND h.uniqueid = :uniqueid  " +
            "ORDER BY h.updatedAt DESC")
    fun findHistoricoWithFilter(
            @Param("idUser") idUser: String,
            @Param("uniqueid") uniqueid: String,
            @Param("limit") limit: Int,
            @Param("offset") offset: Int
    ): List<HistoriesEntity>
}