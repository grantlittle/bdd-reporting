package org.bdd.reporting

import org.bdd.reporting.kafka.KafkaConfigurationAdapter
import org.bdd.reporting.kafka.KafkaSettings
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.kafka.test.rule.KafkaEmbedded.SPRING_EMBEDDED_KAFKA_BROKERS

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
@Order(-1001)
@ComponentScan
open class ServerTestingConfiguration : KafkaConfigurationAdapter {


    @Bean
    @ConfigurationProperties(prefix = "bdd.reporting.kafka")
    open fun kafkaSettings() : KafkaSettings {
        return KafkaSettings(brokers = System.getProperty(SPRING_EMBEDDED_KAFKA_BROKERS))
    }
}