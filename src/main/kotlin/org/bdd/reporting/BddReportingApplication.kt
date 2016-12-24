package org.bdd.reporting

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso

@SpringBootApplication
@EnableOAuth2Sso
open class BddReportingApplication {

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(BddReportingApplication::class.java, *args)
        }
    }
}

