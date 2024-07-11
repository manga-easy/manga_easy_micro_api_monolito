package br.com.lucascm.mangaeasy.micro_api_monolito.core.service.buckets

import com.oracle.bmc.ConfigFileReader
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider
import com.oracle.bmc.objectstorage.ObjectStorage
import com.oracle.bmc.objectstorage.ObjectStorageClient
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest
import com.oracle.bmc.objectstorage.requests.PutObjectRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class BucketService {
    companion object {
        const val NAME_SPACE = "axs7rpnviwd0"
    }

    @Autowired
    lateinit var env: Environment

    private fun getObjectStorage(): ObjectStorage {
        //load config file
        val configFile: ConfigFileReader.ConfigFile = ConfigFileReader
            .parse(env.getProperty("bucket.config"), "DEFAULT")//OracleIdentityCloudService
        val provider = ConfigFileAuthenticationDetailsProvider(configFile)
        //build and return client
        return ObjectStorageClient.builder()
            .isStreamWarningEnabled(false)
            .build(provider)
    }

    fun saveImage(objectName: String, file: MultipartFile, bucketName: String) {
        val configuration = getObjectStorage()
        val inputStream: InputStream = file.inputStream
        //build upload request
        val putObjectRequest: PutObjectRequest = PutObjectRequest.builder()
            .namespaceName(NAME_SPACE)
            .bucketName(bucketName)
            .objectName(objectName)
            .contentLength(file.size)
            .contentType(file.contentType)
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

    fun deleteImage(objectName: String, bucketName: String) {
        val configuration = getObjectStorage()
        //build upload request
        val putObjectRequest: DeleteObjectRequest = DeleteObjectRequest.builder()
            .namespaceName(NAME_SPACE)
            .bucketName(bucketName)
            .objectName(objectName)
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

    fun getLinkImage(objectName: String, bucketName: String): String {
        val configuration = getObjectStorage()
        try {
            // Construa a URL base do serviço Object Storage
            val baseUrl = configuration.endpoint
            // Combinar a URL base e a URL do objeto para obter o link final
            return "${baseUrl}/n/${NAME_SPACE}/b/${bucketName}/o/${objectName}"
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        } finally {
            configuration.close()
        }
    }
}