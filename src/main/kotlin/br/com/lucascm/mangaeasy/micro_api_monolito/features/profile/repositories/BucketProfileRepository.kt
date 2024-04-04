package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import com.oracle.bmc.ConfigFileReader
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider
import com.oracle.bmc.objectstorage.ObjectStorage
import com.oracle.bmc.objectstorage.ObjectStorageClient
import com.oracle.bmc.objectstorage.requests.PutObjectRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

const val LIMIT_FILE_SIZE_GOLD = 3000000
const val LIMIT_FILE_SIZE_IRON = 2000000
const val LIMIT_FILE_SIZE_COPPER = 1000000
const val Membro_Cobre = "61b12f7a0ff25"
const val Membro_Prata = "61b12fddd7201"
const val Membro_Ouro = "61b1302ef0d76"
const val Membro_Platina = "61b130768bd07"
const val Membro_Ferro = "627daca5e13281a2e330"

val TYPE_CONTENT_IMAGE = listOf("JPG", "GIF", "PNG", "JPEG")
const val namespaceName = "axs7rpnviwd0"
const val bucketName = "manga-easy-profile"

@Repository
class BucketProfileRepository {
    @Autowired
    lateinit var usersAchievementsRepository: UsersAchievementsRepository
    fun saveImage(userID: String, file: MultipartFile, contentType: String) {
        // valida os requesitos para ter imagem e o tipo
        validateImage(file, userID)
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
            file.inputStream.close()
            configuration.close()
        }
    }

    fun getLinkImage(userID: String): String {
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
        return ObjectStorageClient.builder()
            .isStreamWarningEnabled(false)
            .build(provider)
    }

    private fun validateImage(file: MultipartFile, userID: String) {
        val limit = getLimitFileByDontion(userID)
        if (file.size > limit) throw BusinessException("Imagem maior que o permetido: ${limit.toString()[0]}mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) throw BusinessException("Tipo de arquivo não permitido.")
        if (typeImage == "GIF" && LIMIT_FILE_SIZE_GOLD != limit) {
            throw BusinessException("Tipo de arquivo permitido apenas para doadores gold e platina.")
        }
    }

    private fun getLimitFileByDontion(userID: String): Int {
        val achievements = usersAchievementsRepository.findAllByUserid(userID)
        var limit = 0
        for (achievement in achievements) {
            if (achievement.idemblema == Membro_Ouro || achievement.idemblema == Membro_Platina) {
                limit = LIMIT_FILE_SIZE_GOLD
                break
            }
            if (achievement.idemblema == Membro_Ferro || achievement.idemblema == Membro_Prata) {
                if (limit < LIMIT_FILE_SIZE_IRON) {
                    limit = LIMIT_FILE_SIZE_IRON
                }
            }
            if (achievement.idemblema == Membro_Cobre) {
                if (limit < LIMIT_FILE_SIZE_COPPER) {
                    limit = LIMIT_FILE_SIZE_COPPER
                }
            }
        }
        if (limit == 0) {
            throw BusinessException("Para ter Imagem precisa ser pelo menos Doador Bronze")
        }
        return limit
    }
}