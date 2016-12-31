package org.bdd.reporting

import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

/**
 * Created by Grant Little grant@grantlittle.me
 */
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = arrayOf(ServerTestingConfiguration::class, BddReportingApplication::class))
@ActiveProfiles("noauth")
@ComponentScan
open abstract class AbstractStepDefs