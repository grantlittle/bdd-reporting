package org.bdd.reporting

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serializer

/**
 * Created by Grant Little grant@grantlittle.me
 */
class JsonSerializer : Serializer<Any> {

    private val objectMapper : ObjectMapper = ObjectMapper()

    init {
    }

    override fun serialize(key: String?, value: Any?): ByteArray {
        return objectMapper.writeValueAsBytes(value)
    }

    override fun close() {
    }

    override fun configure(p0: MutableMap<String, *>?, p1: Boolean) {
    }
}