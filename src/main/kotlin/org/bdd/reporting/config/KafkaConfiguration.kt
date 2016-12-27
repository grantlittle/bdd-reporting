package org.bdd.reporting.config

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringSerializer
import org.bdd.reporting.JsonSerializer
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
    open fun cucumberKafkaProducer(kafkaSettings: KafkaSettings) : KafkaProducer<String, Any> {
        val props = Properties()
        props.put("bootstrap.servers", kafkaSettings.bootstrapServers())
        props.put("key.serializer", StringSerializer::class.java.name)
        props.put("value.serializer", JsonSerializer::class.java.name)
        return KafkaProducer(props)
    }

}