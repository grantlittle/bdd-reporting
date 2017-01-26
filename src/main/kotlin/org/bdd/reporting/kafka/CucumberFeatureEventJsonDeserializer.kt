package org.bdd.reporting.kafka

//import com.fasterxml.jackson.databind.ObjectMapper
//import org.apache.kafka.common.serialization.Deserializer
//import org.bdd.reporting.events.CucumberFeatureEvent
//
///**
// * Created by Grant Little grant@grantlittle.me
// */
//class CucumberFeatureEventJsonDeserializer : Deserializer<CucumberFeatureEvent> {
//
//    private val objectMapper : ObjectMapper = ObjectMapper()
//    override fun configure(p0: MutableMap<String, *>?, p1: Boolean) {
//    }
//
//    override fun close() {
//    }
//
//    override fun deserialize(p0: String?, p1: ByteArray?): CucumberFeatureEvent {
//        return objectMapper.readValue(p1, CucumberFeatureEvent::class.java)
//    }
//}