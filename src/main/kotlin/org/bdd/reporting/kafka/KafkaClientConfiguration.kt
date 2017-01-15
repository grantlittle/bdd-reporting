package org.bdd.reporting.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.bdd.reporting.JsonSerializer
import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.events.CucumberFeatureEvent
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.test.rule.KafkaEmbedded.SPRING_EMBEDDED_KAFKA_BROKERS
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
open class KafkaClientConfiguration {

    @Bean
    open fun kafkaProducer(kafkaSettings: KafkaSettings) : KafkaProducer<String, Any> {
        val props = Properties()
        props.put("bootstrap.servers", kafkaSettings.brokers)
        props.put("key.serializer", StringSerializer::class.java.name)
        props.put("value.serializer", JsonSerializer::class.java.name)
        return KafkaProducer(props)
    }

    @Bean(name = arrayOf("CucumberFeatureManagedConsumer"))
    open fun cucumberKafkaConsumer(kafkaSettings: KafkaSettings) : ManagedKafkaConsumer<String, CucumberFeatureEvent> {
        val props = mutableMapOf(
                Pair("bootstrap.servers", kafkaSettings.brokers),
                Pair("key.deserializer", StringDeserializer::class.java.name),
                Pair("value.deserializer", CucumberFeatureEventJsonDeserializer::class.java.name),
                //                Pair("partition.assignment.strategy", "range"),
                Pair("group.id", "cucumber-common")
        )
        return ManagedKafkaConsumer(props, setOf("cucumber-features"))
    }

    @Bean(name = arrayOf("CommonFeatureManagedConsumer"))
    open fun commonFeatureKafkaConsumer(kafkaSettings: KafkaSettings) : ManagedKafkaConsumer<String, CommonFeature> {
        val props = mutableMapOf(
                Pair("bootstrap.servers", kafkaSettings.brokers),
                Pair("key.deserializer", StringDeserializer::class.java.name),
                Pair("value.deserializer", CommonFeatureJsonDeserializer::class.java.name),
                //                Pair("partition.assignment.strategy", "range"),
                Pair("group.id", "common-elastic")
        )
        return ManagedKafkaConsumer(props, setOf("common-features"))
    }


}

@Configuration
@ConfigurationProperties(prefix = "bdd.reporting.kafka")
@EnableConfigurationProperties(KafkaSettings::class)
open class KafkaSettings(var brokers : String = System.getProperty(SPRING_EMBEDDED_KAFKA_BROKERS) ?: "")
