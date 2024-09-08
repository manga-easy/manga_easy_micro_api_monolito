package br.com.lucascm.mangaeasy.micro_api_monolito.features.achievements.entities

import br.com.lucascm.mangaeasy.micro_api_monolito.core.entities.BusinessException

data class CreateAchievementsDto(
    val name: String = "",
    val rarity: String = "",
    val description: String = "",
    val url: String = "",
    val benefits: String = "",
    val reclaim: Boolean = false,
    val category: String = "",
) {
    fun handlerValidateEntity() {
        // Validar campos obrigatórios
        validateNonEmptyField(category, "categoria")
        validateNonEmptyField(benefits, "benefícios")
        validateNonEmptyField(description, "descrição")
        validateNonEmptyField(name, "nome")
        validateNonEmptyField(rarity, "raridade")
        validateNonEmptyField(category, "tipo")
        // Verificar disponibilidade
        if ((category == "doacao" || category == "rank") && reclaim) {
            throw BusinessException("Esse tipo de emblema não pode estar disponível")
        }
    }

    private fun validateNonEmptyField(campo: String, nomeCampo: String) {
        if (campo.isEmpty()) {
            throw BusinessException(String.format("O campo %s não pode ser vazio", nomeCampo))
        }
    }
}