package org.bdd.reporting

import kafka.server.KafkaConfig
import kafka.server.KafkaServerStartable
import org.apache.commons.io.IOUtils
import org.apache.curator.test.TestingServer
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.io.Resource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.SocketUtils
import org.springframework.web.client.RestClientException
import java.nio.file.Files


/**
 * Created by Grant Little grant@grantlittle.me
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan
class SpringTests {

    @Autowired
    val restTemplate: TestRestTemplate? = null

    @Value("classpath:cucumber-output.json")
    private val sampleCucumberJson: Resource? = null

    @Test
    @Throws(RestClientException::class)
    fun testOne() {
        val json = IOUtils.toString(sampleCucumberJson!!.inputStream)
        val headers = HttpHeaders()
        headers["Content-type"] = "application/json"
        val entity = HttpEntity<String>(json, headers)
        val response = restTemplate!!.exchange("/api/1.0/features/cucumber", HttpMethod.PUT, entity, Any::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @TestConfiguration
    internal open class MyTestConfiguration(val env : ConfigurableEnvironment) {

        @Bean
        open fun kafka() : KafkaServerStartable {
            val zookeeperServer = zookeeper()
            val logsDir = Files.createTempDirectory(MyTestConfiguration::class.java.simpleName)
            logsDir.toFile().deleteOnExit()

            val kafkaProperties = mutableMapOf<String, Any>()
            kafkaProperties["zookeeper.connect"] = "localhost:${zookeeperServer.port}"
            kafkaProperties["broker.id"] = "1"
            kafkaProperties["logs.dir"] = logsDir.toAbsolutePath()
            kafkaProperties["port"] = SocketUtils.findAvailableTcpPort()
            val kafkaConfig = KafkaConfig(kafkaProperties)

            return KafkaServerStartable(kafkaConfig)
        }

        @Bean
        open fun zookeeper() : TestingServer {
            val server = TestingServer()
            env.systemProperties.put("spring.cloud.zookeeper.connect-string", "localhost:${server.port}")
            return server
        }


    }
}