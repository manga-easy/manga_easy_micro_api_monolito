package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository : MongoRepository<ProfileEntity, Long> {
    fun findByUserID(userID: String): ProfileEntity?
}