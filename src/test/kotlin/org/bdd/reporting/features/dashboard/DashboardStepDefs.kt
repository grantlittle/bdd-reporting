package org.bdd.reporting.features.dashboard

import cucumber.api.PendingException
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.apache.commons.io.IOUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
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
open class DashboardStepDefs : AbstractStepDefs() {

    private val log : Log = LogFactory.getLog(DashboardStepDefs::class.java)

    @Autowired
    var restTemplate: TestRestTemplate? = null

    private var entity : HttpEntity<String>? = null

    private var responseEntity : ResponseEntity<Any>? = null

    @Value("classpath:cucumber-output.json")
    var sampleCucumberJson : Resource? = null

    @Value("classpath:pickles-output.json")
    var samplePicklesJson : Resource? = null


    @Given("^a cucumber json report file$")
    fun aCucumberJsonReportFile() {

        val json = IOUtils.toString(sampleCucumberJson!!.inputStream)
        val headers = HttpHeaders()
        headers["Content-type"] = "application/json"
        entity = HttpEntity<String>(json, headers)
    }

    @Given("^a pickles json report file$")
    fun aPicklesJsonReportFile() {

        val json = IOUtils.toString(samplePicklesJson!!.inputStream)
        val headers = HttpHeaders()
        headers["Content-type"] = "application/json"
        entity = HttpEntity<String>(json, headers)
    }


    @When("^the cucumber report file is uploaded$")
    fun theCucumberReportFileIsUploaded()  {
        responseEntity = restTemplate!!.exchange("/api/1.0/features/cucumber", HttpMethod.PUT, entity, Any::class.java)
        log.info("Cucumber file has been sent to kafka")
    }

    @When("^the pickles report file is uploaded$")
    fun thePicklesReportFileIsUploaded()  {
        responseEntity = restTemplate!!.exchange("/api/1.0/features/pickles", HttpMethod.PUT, entity, Any::class.java)
        log.info("Pickles file has been sent to kafka")
    }


    @Then("^we should receive a positive acknowledgement from the system$")
    fun theTestResultsShouldAppearInTheTool() {
        assertEquals(HttpStatus.OK, responseEntity!!.statusCode)
        log.info("Received 200 from server")
    }

    @Given("^the default test set has been uploaded$")
    @Throws(Throwable::class)
    fun the_default_test_set_has_been_uploaded() {
        theCucumberReportFileIsUploaded()
    }

    @When("^the dashboard is displayed$")
    @Throws(Throwable::class)
    fun the_dashboard_is_displayed() {
//        restTemplate!!.getForObject("")
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @Then("^the following data should be displayed$")
    @Throws(Throwable::class)
    fun the_following_data_should_be_displayed(data : List<Map<String, String>>) {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }
}