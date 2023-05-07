package br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.seasons.entities.SeasonsEntity
import org.springframework.data.jpa.repository.JpaRepository



interface SeasonsRepository : JpaRepository<SeasonsEntity, Long> {

}