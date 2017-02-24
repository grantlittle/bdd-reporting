package org.bdd.reporting

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.Resource
import org.springframework.web.client.RestClientException


/**
 * Created by Grant Little grant@grantlittle.me
 */

class SpringTests {

    @Autowired
    val restTemplate: TestRestTemplate? = null


    @Value("classpath:cucumber-output.json")
    private val sampleCucumberJson: Resource? = null

    @Test
    @Throws(RestClientException::class)
    fun testOne() {
    }
}