package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "notification")
data class NotificationsEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    val title: String = "",
    val message: String? = null,
    val image: String? = null,
    @Column(name = "created_at", nullable = false)
    val createdAt: Long = 0,
    val status: NotificationStatus = NotificationStatus.SUCCESS,
)