package org.bdd.reporting

import kafka.server.KafkaConfig
import kafka.server.KafkaServerStartable
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.curator.test.TestingServer
import org.bdd.reporting.config.ZooKeeperSettings
import org.bdd.reporting.kafka.KafkaClientConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.env.ConfigurableEnvironment
import java.nio.file.Files

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
@Order(Int.MIN_VALUE)
open class ServerTestingConfiguration {

    @TestConfiguration
    @Order(Int.MIN_VALUE+1)
    internal open class TestConfig(val env : ConfigurableEnvironment,
                                   @Qualifier("ZooKeeperSettings")val zookeeperSettings : ZooKeeperSettings,
                                   val kafkaClientSettings: KafkaClientConfiguration.KafkaSettings) {

        private val log : Log = LogFactory.getLog(TestConfig::class.java)

        @Bean
        open fun kafka() : KafkaServerStartable {
            log.info("Starting kafka")
            val zookeeperServer = zookeeper()
            val logsDir = Files.createTempDirectory(TestConfig::class.java.simpleName)
            logsDir.toFile().deleteOnExit()

            val kafkaProperties = mutableMapOf<String, Any>()
            kafkaProperties["zookeeper.connect"] = "localhost:${zookeeperServer.port}"
            kafkaProperties["broker.id"] = "1"
            kafkaProperties["logs.dir"] = logsDir.toAbsolutePath()
            kafkaProperties["port"] = kafkaClientSettings.port
            kafkaProperties["queue.buffering.max.ms"] = 10
            kafkaProperties["queue.buffering.max.messages"] = 1
            val kafkaConfig = KafkaConfig(kafkaProperties)

            val startable = KafkaServerStartable(kafkaConfig)
            startable.startup()
            return startable
        }

        @Bean
        open fun zookeeper() : TestingServer {
            log.info("Starting zookeeper")
            val server = TestingServer()
            env.systemProperties.put("spring.cloud.zookeeper.connect-string", "localhost:${server.port}")
            zookeeperSettings.connectString = "localhost:${server.port}"
            server.start()
            return server
        }


    }

}