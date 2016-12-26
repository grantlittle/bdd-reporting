package org.bdd.reporting

import kafka.server.KafkaConfig
import kafka.server.KafkaServerStartable
import org.apache.curator.test.TestingServer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.util.SocketUtils
import java.nio.file.Files

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
open class MyConfiguration {

    @TestConfiguration
    internal open class TestConfig(val env : ConfigurableEnvironment) {

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
            val logsDir = Files.createTempDirectory(TestConfig::class.java.simpleName)
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