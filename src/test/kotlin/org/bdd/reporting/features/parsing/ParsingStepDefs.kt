package org.bdd.reporting.features.parsing

import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.apache.commons.io.IOUtils
import org.bdd.reporting.AbstractStepDefs
import org.junit.Assert.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.Resource
import org.springframework.http.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
open class ParsingStepDefs : AbstractStepDefs() {

    @Autowired
    var restTemplate: TestRestTemplate? = null

    private var entity : HttpEntity<String>? = null

    private var responseEntity : ResponseEntity<Any>? = null

    @Value("classpath:cucumber-output.json")
    var sampleCucumberJson : Resource? = null

    @Given("^a cucumber json report file$")
    fun aCucumberJsonReportFile() {

        val json = IOUtils.toString(sampleCucumberJson!!.inputStream)
        val headers = HttpHeaders()
        headers["Content-type"] = "application/json"
        entity = HttpEntity<String>(json, headers)
    }

    @When("^the cucumber report file is uploaded$")
    fun theCucumberReportFileIsParsed()  {
        responseEntity = restTemplate!!.exchange("/api/1.0/features/cucumber", HttpMethod.PUT, entity, Any::class.java)
    }

    @Then("^we should receive a positive acknowledgement from the system$")
    fun theTestResultsShouldAppearInTheTool() {
        assertEquals(HttpStatus.OK, responseEntity!!.statusCode)
    }

}