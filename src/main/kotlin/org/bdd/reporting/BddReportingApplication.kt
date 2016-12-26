package org.bdd.reporting

import org.bdd.reporting.web.rest.cucumber.CucumberFeatureRestController
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackageClasses = arrayOf(BddReportingApplication::class, CucumberFeatureRestController::class))
//@EnableOAuth2Sso
open class BddReportingApplication {

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(BddReportingApplication::class.java, *args)
        }
    }
}

