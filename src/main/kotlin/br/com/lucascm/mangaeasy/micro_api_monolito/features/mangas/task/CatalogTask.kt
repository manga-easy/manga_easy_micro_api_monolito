package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.task

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.GenderEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.entities.MangaDetailsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.LikeMangaRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.hosts.repositories.MangaDetailsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.ViewMangaRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
class CatalogTask {
    private val log = LoggerFactory.getLogger(CatalogTask::class.java)

    @Autowired
    lateinit var mangaDetailsRepository: MangaDetailsRepository

    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var getUidByFeature: GetUidByFeature

    @Autowired
    lateinit var viewMangaRepository: ViewMangaRepository

    @Autowired
    lateinit var likeMangaRepository: LikeMangaRepository

    @Scheduled(cron = "0 0 4 * * *")
    fun reportCurrentTime() {
        log.info("------------------ Initial CatalogTask --------------")
        chainDetailsCache(0)
        log.info("------------------ Finish CatalogTask --------------")
    }

    private fun chainDetailsCache(page: Int) {
        val result = mangaDetailsRepository.findAll(PageRequest.of(page, 100))
        log.info("------------------Page: $page, quantity: ${result.content.size}")
        for (item in result.content) {
            if (item == null) continue
            updateCatalog(manga = item)
        }
        if (!result.isLast) chainDetailsCache(1 + page)
    }

    fun updateCatalog(manga: MangaDetailsEntity) {
        try {
            log.info("Update manga: {}", manga.data.title)
            val catalog = catalogRepository.findByUniqueid(manga.data.uniqueid)
            val totalLikes = likeMangaRepository.countByUniqueid(manga.data.uniqueid)
            val totalViews = viewMangaRepository.countByUniqueid(manga.data.uniqueid)
            if (catalog == null) {
                catalogRepository.save(
                    CatalogEntity(
                        name = manga.data.title,
                        updatedAt = Date().time,
                        uniqueid = manga.data.uniqueid,
                        createdAt = Date().time,
                        author = manga.data.autor,
                        synopsis = manga.data.sinopse,
                        genres = convertGenders(manga.data.generos),
                        lastChapter = manga.data.capitulos.last().title,
                        ratio = 0.0,
                        scans = manga.data.scans,
                        thumb = manga.data.capa,
                        totalViews = totalViews,
                        year = manga.data.ano.toLongOrNull(),
                        uid = getUidByFeature.get("catalog"),
                        totalLikes = totalLikes,
                    )
                )
            } else {
                catalogRepository.save(
                    catalog.copy(
                        updatedAt = Date().time,
                        thumb = manga.data.capa,
                        lastChapter = manga.data.capitulos.last().title,
                        genres = manga.data.generos.joinToString(separator = "<>", transform = { t -> t.title }),
                        scans = manga.data.scans,
                        synopsis = manga.data.sinopse,
                        year = manga.data.ano.toLongOrNull(),
                        author = manga.data.autor,
                        totalLikes = totalLikes,
                        totalViews = totalViews,
                    )
                )
            }
        } catch (e: Exception) {
            log.error(e.message, e)
        }
    }

    fun convertGenders(genders: List<GenderEntity>): String {
        return genders.joinToString(separator = "<>", transform = { t -> t.title.replace(" ", "-") })
    }

    @Scheduled(cron = "0 0 4 * * *")
    fun deleteMangaInative() {
        log.info("------------------ Initial deleteMangaInative --------------")
        val result = catalogRepository.deleteMangaInactive()
        log.info("------- {}", result)
        log.info("------------------ Finish deleteMangaInative --------------")
    }

}