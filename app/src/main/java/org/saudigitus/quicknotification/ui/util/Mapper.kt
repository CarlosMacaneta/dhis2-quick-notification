package org.saudigitus.quicknotification.ui.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object Mapper {

    @Suppress("UNCHECKED_CAST")
     private fun <T> ObjectMapper.convert(
        k: kotlin.reflect.KClass<*>,
        fromJson: (JsonNode) -> T,
        toJson: (T) -> String,
        isUnion: Boolean = false
    ) = registerModule(
        SimpleModule().apply {
        addSerializer(k.java as Class<T>, object : StdSerializer<T>(k.java as Class<T>) {
            override fun serialize(
                value: T,
                gen: JsonGenerator,
                provider: SerializerProvider
            ) = gen.writeRawValue(toJson(value))
        })
        addDeserializer(k.java as Class<T>, object : StdDeserializer<T>(k.java as Class<T>) {
            override fun deserialize(
                p: JsonParser,
                ctxt: DeserializationContext
            ) = fromJson(p.readValueAsTree())
        })
    })


    fun translateJsonToObject(): ObjectMapper {
        return jacksonObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}