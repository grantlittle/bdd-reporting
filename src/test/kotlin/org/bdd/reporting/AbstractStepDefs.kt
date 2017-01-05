package org.bdd.reporting

import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration


/**
 * Created by Grant Little grant@grantlittle.me
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = arrayOf(ServerTestingConfiguration::class), loader = SpringBootContextLoader::class)
@ActiveProfiles("noauth")
@ComponentScan
open abstract class AbstractStepDefs