package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.entities.BannersEntity
import org.springframework.data.jpa.repository.JpaRepository



interface BannersRepository : JpaRepository<BannersEntity, Long> {

}