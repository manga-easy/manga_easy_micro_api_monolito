package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.task

import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.GetUidByFeature
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleEnum
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.toggle.ToggleService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.CatalogEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.GenderEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.entities.MandaDetailsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.CatalogRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.LatestMangaRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.repositories.MangaDetailsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class CatalogTask {
    private val log = LoggerFactory.getLogger(CatalogTask::class.java)

    @Autowired
    lateinit var mangaDetailsRepository: MangaDetailsRepository

    @Autowired
    lateinit var toggleService: ToggleService

    @Autowired
    lateinit var latestMangaRepository: LatestMangaRepository

    @Autowired
    lateinit var catalogRepository: CatalogRepository

    @Autowired
    lateinit var getUidByFeature: GetUidByFeature

    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
    fun reportCurrentTime() {
        val toggle = toggleService.getToggle<String>(ToggleEnum.currentVersionApp)
        log.info("------------------ Initial CatalogTask --------------")
        val result = mangaDetailsRepository.findVersionAppByOrderByCreatAtDesc(toggle)
        log.info("------------------ toggle $toggle")
        for (item in result) {
            if (item != null) {
                updateCatalog(item)
            }
        }
        log.info("------------------ Finish CatalogTask --------------")
    }

    fun updateCatalog(manga: MandaDetailsEntity) {
        try {
            log.info("Update manga: {}", manga.data.title)
            var catalog = catalogRepository.findByUniqueid(manga.uniqueid)
            if (catalog == null) {
                catalogRepository.save(
                    CatalogEntity(
                        name = manga.data.title,
                        updatedAt = Date().time,
                        uniqueid = manga.uniqueid,
                        createdAt = Date().time,
                        author = manga.data.autor,
                        synopsis = manga.data.sinopse,
                        genres = convertGenders(manga.data.generos),
                        lastChapter = manga.data.capitulos.last().title,
                        ratio = 0.0,
                        scans = manga.data.scans,
                        thumb = manga.data.capa,
                        totalViews = 0,
                        year = manga.data.ano.toLongOrNull(),
                        uid = getUidByFeature.get("catalog")
                    )
                )
            } else {
                catalogRepository.save(
                    catalog.copy(
                        updatedAt = Date().time,
                        thumb = manga.data.capa,
                        lastChapter = manga.data.capitulos.last().title,
                        genres = manga.data.generos.joinToString(separator = "<>", transform = { t -> t.title }),
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