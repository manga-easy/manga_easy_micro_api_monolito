package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.controllers

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException
import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.UserAuth
import br.com.lucascm.mangaeasy.micro_api_monolito.core.service.HandlerPermissionUser
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.AchievementsEntity
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities.CreateAchievementsDto
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.AchievementsRepository
import br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.repositories.BucketAchievementsRepository
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/achievements")
@Tag(name = "Achievements")
class AchievementsController {
    @Autowired
    lateinit var achievementsRepository: AchievementsRepository

    @Autowired
    lateinit var bucketAchievementsRepository: BucketAchievementsRepository

    @Autowired
    lateinit var handlerPermissionUser: HandlerPermissionUser

    @GetMapping("/v1")
    fun list(
        @RequestParam reclaim: Boolean?,
        @RequestParam page: Int?,
        @RequestParam category: String?,
        @RequestParam rarity: String?,
        @RequestParam name: String?
    ): List<AchievementsEntity> {
        return achievementsRepository.findAll(
            Specification(fun(
                root: Root<AchievementsEntity>,
                _: CriteriaQuery<*>,
                builder: CriteriaBuilder,
            ): Predicate? {
                val predicates = mutableListOf<Predicate>()
                if (reclaim != null) {
                    predicates.add(
                        builder.equal(
                            root.get<Boolean>(AchievementsEntity::reclaim.name),
                            reclaim
                        )
                    )
                }
                if (category != null) {
                    predicates.add(
                        builder.equal(
                            root.get<String>(AchievementsEntity::category.name),
                            category
                        )
                    )
                }
                if (rarity != null) {
                    predicates.add(
                        builder.equal(
                            root.get<String>(AchievementsEntity::rarity.name),
                            rarity
                        )
                    )
                }
                if (name != null) {
                    predicates.add(
                        builder.like(
                            builder.lower(root.get(AchievementsEntity::name.name)),
                            "%" + name.lowercase() + "%"
                        )
                    )
                }
                return builder.and(*predicates.toTypedArray())
            }),
            PageRequest.of(
                page ?: 0, 25, Sort.by(
                    Sort.Direction.DESC,
                    AchievementsEntity::updatedAt.name
                )
            )
        ).content
    }

    @GetMapping("/v1/users/{userId}")
    fun listByUser(@PathVariable userId: String): List<AchievementsEntity> {
        return achievementsRepository.findByUser(userId)
    }

    @GetMapping("/v1/{id}")
    fun getOne(@PathVariable id: String): AchievementsEntity {
        return achievementsRepository.findById(id).getOrNull()
            ?: throw BusinessException("Emblema não encontrado")
    }

    @PostMapping("/v1")
    fun create(
        @RequestBody body: CreateAchievementsDto,
        @AuthenticationPrincipal userAuth: UserAuth
    ): AchievementsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        body.handlerValidateEntity()
        return achievementsRepository.save(
            AchievementsEntity(
                createdAt = Date().time,
                updatedAt = Date().time,
                name = body.name,
                benefits = body.benefits,
                category = body.category,
                description = body.description,
                percentRarity = 0.0,
                rarity = body.rarity,
                reclaim = body.reclaim,
                totalAcquired = 0
            )
        )
    }

    @PutMapping("/v1/{id}")
    fun update(
        @RequestBody body: CreateAchievementsDto,
        @AuthenticationPrincipal userAuth: UserAuth,
        @PathVariable id: String,
    ): AchievementsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        body.handlerValidateEntity()
        val resultFind = achievementsRepository.findById(id).getOrNull()
            ?: throw BusinessException("Emblema não encontrado")
        return achievementsRepository.save(
            resultFind.copy(
                updatedAt = Date().time,
                name = body.name,
                benefits = body.benefits,
                category = body.category,
                description = body.description,
                reclaim = body.reclaim,
                rarity = body.rarity
            )
        )
    }

    @PutMapping("/v1/{id}/image")
    fun uploadImage(
        @RequestPart file: MultipartFile,
        @PathVariable id: String,
        @AuthenticationPrincipal userAuth: UserAuth
    ): AchievementsEntity {
        handlerPermissionUser.handleIsAdmin(userAuth)
        val find: AchievementsEntity = achievementsRepository.findById(id).getOrNull()
            ?: throw BusinessException("Emblema não encontrado")
        bucketAchievementsRepository.saveImage(id, file, file.contentType!!)
        val imageResult = bucketAchievementsRepository.getLinkImage(id)
        return achievementsRepository.save(find.copy(url = imageResult, updatedAt = Date().time))
    }
}