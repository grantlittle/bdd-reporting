package org.bdd.reporting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
//@Order(-1001)
@ComponentScan
@SpringBootApplication
@EnableBddReporting
open class ServerTestingConfiguration