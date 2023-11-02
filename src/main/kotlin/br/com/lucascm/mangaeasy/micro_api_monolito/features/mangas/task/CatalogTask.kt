package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.task

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.MandaDetailsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.MangaDetailsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*
@Component
class CatalogTask{
    private val log = LoggerFactory.getLogger(CatalogTask::class.java)
    private val dateFormat = SimpleDateFormat("HH:mm:ss")
    @Autowired
    lateinit var mangaDetailsRepository: MangaDetailsRepository
    @Autowired
    lateinit var catalogRepository: CatalogRepository
    @Autowired
    lateinit var getUidByFeature: GetUidByFeature
    @Scheduled(fixedRate = 10000)
    fun reportCurrentTime() {
        log.info("------------------ Initial CatalogTask --------------")
        val result = mangaDetailsRepository.findByOrderByCreatAtDesc()
        result.forEach{ it -> updateCatalog(it) }
        log.info("------------------ Finish CatalogTask --------------")
    }

    fun updateCatalog(manga: MandaDetailsEntity){
        try {
            log.info("Update manga: {}", dateFormat.format(Date()))
            var catalog = catalogRepository.findByUniqueid(manga.uniqueid)
            if (catalog == null){
                catalogRepository.save(CatalogEntity(
                    name = manga.data.title,
                    updatedAt = Date().time,
                    uniqueid = manga.uniqueid,
                    createdAt = Date().time,
                    author = manga.data.autor,
                    synopsis = manga.data.sinopse,
                    genres = manga.data.generos.joinToString(separator = "<>", transform =  {t -> t.title}),
                    lastChapter = manga.data.capitulos.last().title,
                    ratio = 0.0,
                    scans = manga.data.scans,
                    thumb = manga.data.capa,
                    totalViews = 0,
                    year = manga.data.ano.toLongOrNull(),
                    uid = getUidByFeature.get("catalog")
                ))
            } else {
                catalogRepository.save(catalog.copy(
                    updatedAt = Date().time,
                    thumb = manga.data.capa,
                    lastChapter = manga.data.capitulos.last().title,
                    genres = manga.data.generos.joinToString(separator = "<>", transform =  {t -> t.title}),
                ))
            }
        }catch (e: Exception){
            log.error(e.message, e)
        }
    }
}