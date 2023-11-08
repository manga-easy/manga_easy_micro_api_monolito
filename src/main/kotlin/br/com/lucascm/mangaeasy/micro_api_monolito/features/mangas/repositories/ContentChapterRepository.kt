package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.ContentChapterEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ContentChapterRepository : MongoRepository<ContentChapterEntity, Long> {
    fun findByIdhostAndUniqueidAndChapterAndVersionApp(idhost: Int, uniqueid: String, chapter: String, versionApp: String?): ContentChapterEntity?
}