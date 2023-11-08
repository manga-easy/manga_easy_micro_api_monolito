package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository


@Repository
interface CatalogRepository : JpaSpecificationExecutor<CatalogEntity>, JpaRepository<CatalogEntity, Long>  {

    fun findByUniqueid(uniqueid: String): CatalogEntity?
}