package br.com.lucascm.mangaeasy.micro_api_monolito.features.recommendations.entities

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException


data class CreateRecommendationDto(
    val uniqueId: String = "",
    val title: String = "",
    val artistId: String,

    ) {
    fun validationValues() {
        if (uniqueId.isEmpty()) {
            throw BusinessException("Campo uniqueId não pode ser vazio")
        }
        if (title.isEmpty()) {
            throw BusinessException("Campo title não pode ser vazio")
        }
        if (artistId.isEmpty()) {
            throw BusinessException("Campo artistid não pode ser vazio")
        }
    }
}