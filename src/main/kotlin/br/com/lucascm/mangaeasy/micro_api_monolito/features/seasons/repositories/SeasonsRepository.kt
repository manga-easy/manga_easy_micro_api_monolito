package br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.entities.SeasonsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface SeasonsRepository : JpaRepository<SeasonsEntity, Long> {
  fun findByUid(uid: String): SeasonsEntity?
  fun findTop1ByOrderByNumberDesc(): SeasonsEntity

  @Query("SELECT MAX(s.number) FROM SeasonsEntity s")
  fun findMaxNumber(): Int
}