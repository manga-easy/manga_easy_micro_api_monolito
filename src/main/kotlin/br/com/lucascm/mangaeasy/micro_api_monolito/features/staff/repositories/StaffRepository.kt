package br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.staff.entities.StaffEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface StaffRepository : JpaRepository<StaffEntity, String> {
    fun findByUserId(userId: String): Optional<StaffEntity>
}
