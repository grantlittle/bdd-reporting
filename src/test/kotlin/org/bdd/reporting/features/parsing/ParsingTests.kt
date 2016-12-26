package org.bdd.reporting.features.parsing

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import kafka.server.KafkaConfig
import kafka.server.KafkaServerStartable
import org.apache.curator.test.TestingServer
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.springframework.util.SocketUtils
import java.nio.file.Files


/**
 * Created by Grant Little grant@grantlittle.me
 */
@RunWith(Cucumber::class)
@CucumberOptions(features = arrayOf("src/test/resources/features/parsing"))
class ParsingTests {


    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpBeforeClass() {

            val zookeeperServer = TestingServer()
            val logsDir = Files.createTempDirectory(ParsingTests::class.java.simpleName)
            logsDir.toFile().deleteOnExit()

            val kafkaProperties = mutableMapOf<String, Any>()
            kafkaProperties["zookeeper.connect"] = "localhost:${zookeeperServer.port}"
            kafkaProperties["broker.id"] = "1"
            kafkaProperties["logs.dir"] = logsDir.toAbsolutePath()
            kafkaProperties["port"] = SocketUtils.findAvailableTcpPort()
            val kafkaConfig = KafkaConfig(kafkaProperties)
            val kafka = KafkaServerStartable(kafkaConfig)


            kafka.startup()
        }
    }


}