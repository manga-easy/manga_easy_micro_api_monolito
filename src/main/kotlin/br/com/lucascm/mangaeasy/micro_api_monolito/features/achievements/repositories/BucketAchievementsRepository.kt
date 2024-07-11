package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.buckets.BucketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile


@Repository
class BucketAchievementsRepository {
    @Autowired
    lateinit var bucketService: BucketService

    companion object {
        const val LIMIT_FILE_SIZE_ACHIEVEMENT = 500000
        const val BUCKET_NAME = "manga-easy-emblemas"
        val TYPE_CONTENT_IMAGE = listOf("JPG", "GIF", "PNG", "JPEG")
    }

    fun saveImage(uid: String, file: MultipartFile, contentType: String) {
        validateImage(file)
        bucketService.saveImage(uid, file, BUCKET_NAME)
    }

    fun getLinkImage(uid: String): String {
        return bucketService.getLinkImage(uid, BUCKET_NAME)
    }

    fun deleteImage(uid: String) {
        bucketService.deleteImage(uid, BUCKET_NAME)
    }

    private fun validateImage(file: MultipartFile) {
        val limit = LIMIT_FILE_SIZE_ACHIEVEMENT
        if (file.size > limit) throw BusinessException("Imagem maior que o permitido: ${limit.toString()[0]}mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) throw BusinessException("Tipo de arquivo não permitido.")
    }
}