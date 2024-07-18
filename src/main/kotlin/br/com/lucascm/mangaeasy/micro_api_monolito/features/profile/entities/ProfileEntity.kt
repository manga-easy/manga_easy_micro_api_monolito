package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities

import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.converters.FavoriteAchievementListConverter
import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.converters.FavoriteMangaListConverter
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "profile")
data class ProfileEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    val role: String = "",
    val biography: String? = null,

    @Column(name = "user_id", unique = true, nullable = false)
    val userId: String = "",

    @Lob
    @Convert(converter = FavoriteMangaListConverter::class)
    @Column(name = "mangas_highlight", columnDefinition = "json")
    val mangasHighlight: List<FavoriteManga> = listOf(),

    @Lob
    @Convert(converter = FavoriteAchievementListConverter::class)
    @Column(name = "achievements_highlight", columnDefinition = "json")
    val achievementsHighlight: List<FavoriteAchievement> = listOf(),
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
    val totalMangaRead: Long = 0,
    val totalXp: Long = 0,
    val totalAchievements: Long = 0,
    val picture: String? = null,
    val name: String? = null,
    val visibleStatics: Boolean = true,
    val visibleAchievements: Boolean = true,
    val visibleMangas: Boolean = true,
)