package br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.converters
import br.com.lucascm.mangaeasy.micro_api_monolito.features.release_notes.entities.FixEntity
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class FixEntityConverter : AttributeConverter<List<FixEntity>, String> {
    private val objectMapper = ObjectMapper()
    override fun convertToDatabaseColumn(attribute: List<FixEntity>?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): List<FixEntity> {
        return objectMapper.readValue(
            dbData,
            objectMapper.typeFactory.constructCollectionType(List::class.java, FixEntity::class.java)
        )
    }
}