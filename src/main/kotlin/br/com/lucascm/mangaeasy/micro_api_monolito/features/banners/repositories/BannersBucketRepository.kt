package br.com.lucascm.mangaeasy.micro_api_monolito.features.banners.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.buckets.BucketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile

class BannersBucketRepository {
    @Autowired
    lateinit var bucketService: BucketService

    companion object {
        val TYPE_CONTENT_IMAGE = listOf("JPG", "GIF", "PNG", "JPEG")
        const val BUCKET_NAME = "manga-easy-banner-anuncios"
        const val LIMIT_FILE_SIZE_RECOMMENDATION = 2000000
    }

    fun saveImage(uniqueId: String, file: MultipartFile) {
        validateImage(file)
        bucketService.saveImage(uniqueId, file, BUCKET_NAME)
    }

    fun getLinkImage(uniqueId: String): String {
        return bucketService.getLinkImage(uniqueId, BUCKET_NAME)
    }

    fun deleteImage(uniqueId: String) {
        bucketService.deleteImage(uniqueId, BUCKET_NAME)
    }

    private fun validateImage(file: MultipartFile) {
        val limit = LIMIT_FILE_SIZE_RECOMMENDATION
        if (file.size > limit) throw BusinessException("Imagem maior que o permitido: ${limit.toString()[0]}mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) throw BusinessException("Tipo de arquivo não permitido.")
    }
}