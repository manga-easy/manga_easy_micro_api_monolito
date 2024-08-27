package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.ProfileV1Entity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileV1Repository : MongoRepository<ProfileV1Entity, String> {
    fun findByIsV2(isV2: Boolean): List<ProfileV1Entity>
}