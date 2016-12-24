package org.bdd.reporting

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class BddReportingApplication

fun main(args: Array<String>) {
    SpringApplication.run(BddReportingApplication::class.java, *args)
}
