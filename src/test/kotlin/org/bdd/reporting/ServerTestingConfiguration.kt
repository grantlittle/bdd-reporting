package org.bdd.reporting

import kafka.server.KafkaConfig
import kafka.server.KafkaServerStartable
import org.apache.curator.test.TestingServer
import org.bdd.reporting.kafka.KafkaConfiguration
import org.bdd.reporting.config.ZooKeeperSettings
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.ConfigurableEnvironment
import java.nio.file.Files

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
open class ServerTestingConfiguration {

    @TestConfiguration
    internal open class TestConfig(val env : ConfigurableEnvironment,
                                   @Qualifier("ZooKeeperSettings")val zookeeperSettings : ZooKeeperSettings,
                                   val kafkaSettings: KafkaConfiguration.KafkaSettings) {

        @Bean
        open fun kafka() : KafkaServerStartable {
            val zookeeperServer = zookeeper()
            val logsDir = Files.createTempDirectory(TestConfig::class.java.simpleName)
            logsDir.toFile().deleteOnExit()

            val kafkaProperties = mutableMapOf<String, Any>()
            kafkaProperties["zookeeper.connect"] = "localhost:${zookeeperServer.port}"
            kafkaProperties["broker.id"] = "1"
            kafkaProperties["logs.dir"] = logsDir.toAbsolutePath()
            kafkaProperties["port"] = kafkaSettings.randomPort()
            val kafkaConfig = KafkaConfig(kafkaProperties)

            return KafkaServerStartable(kafkaConfig)
        }

        @Bean
        open fun zookeeper() : TestingServer {
            val server = TestingServer()
            env.systemProperties.put("spring.cloud.zookeeper.connect-string", "localhost:${server.port}")
            zookeeperSettings.connectString = "localhost:${server.port}"
            return server
        }


    }

}