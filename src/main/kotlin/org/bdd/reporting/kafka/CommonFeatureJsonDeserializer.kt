package org.bdd.reporting.kafka

//import com.fasterxml.jackson.databind.ObjectMapper
//import org.apache.kafka.common.serialization.Deserializer
//import org.bdd.reporting.data.CommonFeature
//
///**
// * Created by Grant Little grant@grantlittle.me
// */
//class CommonFeatureJsonDeserializer : Deserializer<CommonFeature> {
//
//    private val objectMapper : ObjectMapper = ObjectMapper()
//    override fun configure(p0: MutableMap<String, *>?, p1: Boolean) {
//    }
//
//    override fun close() {
//    }
//
//    override fun deserialize(p0: String?, p1: ByteArray?): CommonFeature {
//        return objectMapper.readValue(p1, CommonFeature::class.java)
//    }
//}