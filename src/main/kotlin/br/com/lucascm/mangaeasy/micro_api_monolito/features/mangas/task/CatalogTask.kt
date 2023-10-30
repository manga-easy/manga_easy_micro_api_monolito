package br.com.lucascm.mangaeasy.micro_api_monolito.features.mangas.task

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
    @Scheduled(fixedRate = 5000)
    fun reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(Date()))
        var offset = 0;
        val result = mangaDetailsRepository.findByOrderByCreatatDesc(PageRequest.of(offset, 100))
        result.forEach {
            log.info(it.uniqueid)
        }
    }
}