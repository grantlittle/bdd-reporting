package org.bdd.reporting.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.bdd.reporting.JsonSerializer
import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.events.CucumberFeatureEvent
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.SocketUtils
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
open class KafkaConfiguration {

    @Configuration
    @ConfigurationProperties(prefix = "bdd.reporting.kafka")
    open class KafkaSettings (var port : Int = 9092) {

        fun randomPort() : Int {
            port = SocketUtils.findAvailableTcpPort()
            return port
        }

        fun bootstrapServers() : String {
            return "localhost:$port"
        }
    }

    @Bean()
    open fun kafkaProducer(kafkaSettings: KafkaSettings) : KafkaProducer<String, Any> {
        val props = Properties()
        props.put("bootstrap.servers", kafkaSettings.bootstrapServers())
        props.put("key.serializer", StringSerializer::class.java.name)
        props.put("value.serializer", JsonSerializer::class.java.name)
        return KafkaProducer(props)
    }

    @Bean(name = arrayOf("CucumberFeatureManagedConsumer"))
    open fun cucumberKafkaConsumer(kafkaSettings: KafkaSettings) : ManagedKafkaConsumer<String, CucumberFeatureEvent> {
        val props = mutableMapOf(
                Pair("bootstrap.servers", kafkaSettings.bootstrapServers()),
                Pair("key.deserializer", StringDeserializer::class.java.name),
                Pair("value.deserializer", CucumberFeatureEventJsonDeserializer::class.java.name),
                //                Pair("partition.assignment.strategy", "range"),
                Pair("group.id", "cucumber")
        )
        return ManagedKafkaConsumer(props, setOf("cucumber-features"))
    }

    @Bean(name = arrayOf("CommonFeatureManagedConsumer"))
    open fun commonFeatureKafkaConsumer(kafkaSettings: KafkaSettings) : ManagedKafkaConsumer<String, CommonFeature> {
        val props = mutableMapOf(
                Pair("bootstrap.servers", kafkaSettings.bootstrapServers()),
                Pair("key.deserializer", StringDeserializer::class.java.name),
                Pair("value.deserializer", CommonFeatureJsonDeserializer::class.java.name),
                //                Pair("partition.assignment.strategy", "range"),
                Pair("group.id", "cucumber")
        )
        return ManagedKafkaConsumer(props, setOf("common-features"))
    }

}