package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.buckets.BucketService
import br.com.lucascm.mangaeasy.micro_api_monolito.features.users.repositories.UsersAchievementsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile


@Repository
class BucketProfileRepository {
    @Autowired
    lateinit var bucketService: BucketService

    companion object {
        const val LIMIT_FILE_SIZE_GOLD = 3000000
        const val LIMIT_FILE_SIZE_IRON = 2000000
        const val LIMIT_FILE_SIZE_COPPER = 1000000
        const val Membro_Cobre = "61b12f7a0ff25"
        const val Membro_Prata = "61b12fddd7201"
        const val Membro_Ouro = "61b1302ef0d76"
        const val Membro_Platina = "61b130768bd07"
        const val Membro_Ferro = "627daca5e13281a2e330"
        val TYPE_CONTENT_IMAGE = listOf("JPG", "GIF", "PNG", "JPEG")
        const val BUCKET_NAME = "manga-easy-profile"
    }

    @Autowired
    lateinit var usersAchievementsRepository: UsersAchievementsRepository
    fun saveImage(userID: String, file: MultipartFile) {
        // valida os requesitos para ter imagem e o tipo
        validateImage(file, userID)
        bucketService.saveImage(userID, file, BUCKET_NAME)
    }

    fun getLinkImage(userID: String): String {
        return bucketService.getLinkImage(userID, BUCKET_NAME)
    }

    fun deleteImage(userID: String) {
        bucketService.deleteImage(userID, BUCKET_NAME)
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
        val achievements = usersAchievementsRepository.findAllByUserId(userID)
        var limit = 0
        for (achievement in achievements) {
            if (achievement.achievementId == Membro_Ouro || achievement.achievementId == Membro_Platina) {
                limit = LIMIT_FILE_SIZE_GOLD
                break
            }
            if (achievement.achievementId == Membro_Ferro || achievement.achievementId == Membro_Prata) {
                if (limit < LIMIT_FILE_SIZE_IRON) {
                    limit = LIMIT_FILE_SIZE_IRON
                }
            }
            if (achievement.achievementId == Membro_Cobre) {
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