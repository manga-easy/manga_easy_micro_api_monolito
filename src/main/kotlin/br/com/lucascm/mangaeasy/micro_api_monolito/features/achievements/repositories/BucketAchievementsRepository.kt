package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.controllers.LIMIT_FILE_SIZE_ACHIEVEMENT
import com.oracle.bmc.ConfigFileReader
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider
import com.oracle.bmc.objectstorage.ObjectStorage
import com.oracle.bmc.objectstorage.ObjectStorageClient
import com.oracle.bmc.objectstorage.requests.PutObjectRequest
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream


const val namespaceName = "axs7rpnviwd0"
const val bucketName = "manga-easy-emblemas"
val TYPE_CONTENT_IMAGE = listOf("JPG", "GIF", "PNG", "JPEG")

@Repository
class BucketAchievementsRepository {
    fun saveImage(uid: String, file: MultipartFile, contentType: String) {
        validateImage(file)
        val configuration = getObjectStorage()
        val inputStream: InputStream = file.inputStream

        //build upload request
        val putObjectRequest: PutObjectRequest =
            PutObjectRequest.builder()
                .namespaceName(namespaceName)
                .bucketName(bucketName)
                .objectName(uid)
                .contentLength(file.size)
                .contentType(contentType)
                .putObjectBody(inputStream).build()
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

    fun getLinkImage(uid: String): String {
        val configuration = getObjectStorage()
        try {
            // Construa a URL base do serviço Object Storage
            val baseUrl = configuration.endpoint
            // Combinar a URL base e a URL do objeto para obter o link final
            return "${baseUrl}/n/${namespaceName}/b/${bucketName}/o/${uid}"
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        } finally {
            configuration.close()
        }
    }

    private fun getObjectStorage(): ObjectStorage {
        //load config file
        val configFile: ConfigFileReader.ConfigFile =
            ConfigFileReader.parse("src/main/resources/config", "DEFAULT")//OracleIdentityCloudService
        val provider = ConfigFileAuthenticationDetailsProvider(configFile)
        //build and return client
        return ObjectStorageClient.builder().isStreamWarningEnabled(false).build(provider)
    }

    private fun validateImage(file: MultipartFile) {
        val limit = LIMIT_FILE_SIZE_ACHIEVEMENT
        if (file.size > limit) throw BusinessException("Imagem maior que o permitido: ${limit.toString()[0]}mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) throw BusinessException("Tipo de arquivo não permitido.")
    }
}