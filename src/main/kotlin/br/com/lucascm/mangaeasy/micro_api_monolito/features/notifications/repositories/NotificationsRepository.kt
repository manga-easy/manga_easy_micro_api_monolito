package br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.repositories

import br.com.lucascm.mangaeasy.micro_api_monolito.features.notifications.entities.NotificationsEntity
import org.springframework.data.jpa.repository.JpaRepository


interface NotificationsRepository : JpaRepository<NotificationsEntity, String> {
    fun findTop25ByOrderByCreatedatDesc(): List<NotificationsEntity>
   
}