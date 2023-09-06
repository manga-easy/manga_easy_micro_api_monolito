package br.com.lucascm.mangaeasy.micro_api_monolito.features.histories.entities

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "NA")
class HistoriesEntity: Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var _id: Long? = null

    @Column(nullable = false)
    var idUser: String? = null
        private set

    @Column(nullable = true)
    var uniqueId: String? = null
        private set

    @Column(nullable = false)
    var orderUpdate: Boolean? = null
        private set
}