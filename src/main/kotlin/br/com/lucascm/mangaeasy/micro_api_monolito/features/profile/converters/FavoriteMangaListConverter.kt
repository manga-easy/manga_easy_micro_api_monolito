package br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.converters

import br.com.lucascm.mangaeasy.micro_api_monolito.features.profile.entities.FavoriteManga
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class FavoriteMangaListConverter : AttributeConverter<List<FavoriteManga>, String> {
    private val objectMapper = ObjectMapper()
    override fun convertToDatabaseColumn(attribute: List<FavoriteManga>?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): List<FavoriteManga> {
        return objectMapper.readValue(
            dbData,
            objectMapper.typeFactory.constructCollectionType(List::class.java, FavoriteManga::class.java)
        )
    }
}