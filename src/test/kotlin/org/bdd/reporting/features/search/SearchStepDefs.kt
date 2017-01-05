package org.bdd.reporting.features.search

import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import org.apache.commons.io.IOUtils
import org.bdd.reporting.AbstractStepDefs
import org.bdd.reporting.data.CommonFeature
import org.junit.Assert.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.Resource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

/**
 * Created by Grant Little grant@grantlittle.me
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchStepDefs : AbstractStepDefs() {

    @Autowired
    var restTemplate: TestRestTemplate? = null

    @Value("classpath:cucumber-output.json")
    var sampleCucumberJson : Resource? = null

    private var response : Array<CommonFeature>? = null

    @Given("^some reports have been uploaded$")
    fun some_reports_have_been_uploaded() {
        val json = IOUtils.toString(sampleCucumberJson!!.inputStream)
        val headers = HttpHeaders()
        headers["Content-type"] = "application/json"
        val entity = HttpEntity<String>(json, headers)
        val responseEntity = restTemplate!!.exchange("/api/1.0/features/cucumber", HttpMethod.PUT, entity, Any::class.java)
        assertEquals(HttpStatus.OK, responseEntity!!.statusCode)

    }

    @When("^I search for a term$")
    fun i_search_for_a_term() {
        var count = 0
        while (count < 20 && (response == null || response?.size == 0)) {
            response = restTemplate!!.getForObject("/api/1.0/search?name={name}", Array<CommonFeature>::class.java, "Feature1")
            if (response == null || (response as Array<CommonFeature>).size == 0) {
                Thread.sleep(500)
                count++
            }
        }
    }

    @Then("^I should see all items related to that term in the search results$")
    fun i_should_see_all_items_related_to_that_term_in_the_search_results() {
        assertEquals(1, response?.size)
    }

}