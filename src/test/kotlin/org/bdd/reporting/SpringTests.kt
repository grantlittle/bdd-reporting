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

//        @Value("\${server.port}")
//        var port : Int? = null
//
//        @Configuration
//        @EnableAuthorizationServer
//        internal open class AuthServerConfig : AuthorizationServerConfigurerAdapter() {
//
//            override fun configure(clients: ClientDetailsServiceConfigurer) {
//                clients.inMemory()
//                        .withClient("test").secret("password").scopes("read").autoApprove(true)
//            }
//        }
//
//        @Bean
//        open fun oauthRestTemplate() : OAuth2RestTemplate {
//            val resourceDetails = ResourceOwnerPasswordResourceDetails()
//            resourceDetails.clientId = "test"
//            resourceDetails.clientSecret = "password"
//            resourceDetails.accessTokenUri = "http://localhost:$port/oauth/token"
//            resourceDetails.grantType = "client_credentials"
//            val oauthContext = DefaultOAuth2ClientContext()
//            return OAuth2RestTemplate(resourceDetails, oauthContext)
//        }

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