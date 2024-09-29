package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.converters
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_note.entities.FeatureEntity
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class FeatureEntityConverter : AttributeConverter<List<FeatureEntity>, String> {
    private val objectMapper = ObjectMapper()
    override fun convertToDatabaseColumn(attribute: List<FeatureEntity>?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): List<FeatureEntity> {
        return objectMapper.readValue(
            dbData,
            objectMapper.typeFactory.constructCollectionType(List::class.java, FeatureEntity::class.java)
        )
    }
}