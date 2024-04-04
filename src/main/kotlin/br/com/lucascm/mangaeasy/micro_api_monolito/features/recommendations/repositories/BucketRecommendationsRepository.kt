package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import com.oracle.bmc.ConfigFileReader
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider
import com.oracle.bmc.objectstorage.ObjectStorage
import com.oracle.bmc.objectstorage.ObjectStorageClient
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest
import com.oracle.bmc.objectstorage.requests.PutObjectRequest
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream


const val namespaceName = "axs7rpnviwd0"
const val bucketName = "manga-easy-banners"
const val LIMIT_FILE_SIZE_RECOMMENDATION = 2000000
val TYPE_CONTENT_IMAGE = listOf("JPG", "GIF", "PNG", "JPEG")

@Repository
class BucketRecommendationsRepository {
    fun saveImage(uniqueId: String, file: MultipartFile, contentType: String) {
        validateImage(file)
        val configuration = getObjectStorage()
        val inputStream: InputStream = file.inputStream
        //build upload request
        val putObjectRequest: PutObjectRequest = PutObjectRequest.builder()
            .namespaceName(namespaceName)
            .bucketName(bucketName)
            .objectName(uniqueId)
            .contentLength(file.size)
            .contentType(contentType)
            .putObjectBody(inputStream)
            .build()
        //upload the file
        try {
            configuration.putObject(putObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        } finally {
            file.inputStream.close()
            configuration.close()
        }
    }

    fun getLinkImage(uniqueId: String): String {
        val configuration = getObjectStorage()
        try {
            // Construa a URL base do serviço Object Storage
            val baseUrl = configuration.endpoint
            // Combinar a URL base e a URL do objeto para obter o link final
            return "${baseUrl}/n/${namespaceName}/b/${bucketName}/o/${uniqueId}"
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        } finally {
            configuration.close()
        }
    }

    private fun getObjectStorage(): ObjectStorage {
        //load config file
        val configFile: ConfigFileReader.ConfigFile = ConfigFileReader
            .parse("src/main/resources/config", "DEFAULT")//OracleIdentityCloudService
        val provider = ConfigFileAuthenticationDetailsProvider(configFile)
        //build and return client
        return ObjectStorageClient.builder()
            .isStreamWarningEnabled(false)
            .build(provider)
    }

    fun deleteImage(uniqueId: String) {
        val configuration = getObjectStorage()
        //build upload request
        val putObjectRequest: DeleteObjectRequest = DeleteObjectRequest.builder()
            .namespaceName(namespaceName)
            .bucketName(bucketName)
            .objectName(uniqueId)
            .build()
        //upload the file
        try {
            configuration.deleteObject(putObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        } finally {
            configuration.close()
        }
    }

    private fun validateImage(file: MultipartFile) {
        val limit = LIMIT_FILE_SIZE_RECOMMENDATION
        if (file.size > limit) throw BusinessException("Imagem maior que o permitido: ${limit.toString()[0]}mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) throw BusinessException("Tipo de arquivo não permitido.")
    }
}