package org.bdd.reporting

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableSso
open class BddReportingApplication {

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(BddReportingApplication::class.java, *args)
        }
    }
}

