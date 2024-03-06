package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories

import com.oracle.bmc.ConfigFileReader
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider
import com.oracle.bmc.objectstorage.ObjectStorage
import com.oracle.bmc.objectstorage.ObjectStorageClient
import com.oracle.bmc.objectstorage.requests.PutObjectRequest
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream


const val namespaceName = "axs7rpnviwd0"
const val bucketName = "manga-easy-profile"
@Repository
class BucketRepository {
    fun saveImage(userID: String, file: MultipartFile, contentType: String){
        val configuration = getObjectStorage()
        val inputStream: InputStream = file.inputStream

        //build upload request
        val putObjectRequest: PutObjectRequest = PutObjectRequest.builder()
            .namespaceName(namespaceName)
            .bucketName(bucketName)
            .objectName(userID)
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
            configuration.close()
        }
    }
    fun getLinkImage(userID: String): String{
        val configuration = getObjectStorage()
        try {
           // Construa a URL base do serviço Object Storage
           val baseUrl = configuration.endpoint
           // Combinar a URL base e a URL do objeto para obter o link final
           return "${baseUrl}/n/${namespaceName}/b/${bucketName}/o/${userID}"
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
        return  ObjectStorageClient.builder().build(provider);
    }
}