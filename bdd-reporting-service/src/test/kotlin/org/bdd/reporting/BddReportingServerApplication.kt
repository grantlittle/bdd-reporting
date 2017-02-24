package org.bdd.reporting

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by Grant Little grant@grantlittle.me
 */
@SpringBootApplication(exclude = arrayOf(ServerTestingConfiguration::class))
open class BddReportingServerApplication {

    companion object {
        @JvmStatic
        fun main(args : Array<String> ) {
            SpringApplication.run(arrayOf(BddReportingServerApplication::class.java), args)
        }
    }
}