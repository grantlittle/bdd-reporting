package org.bdd.reporting.features.parsing

import cucumber.api.PendingException
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

/**
 * Created by Grant Little grant@grantlittle.me
 */
class ParsingStepDefs {

    @Given("something")
    fun something() {

    }

    @Given("^a cucumber json report file$")
    fun aCucumberJsonReportFile() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @When("^the cucumber report file is parsed$")
    fun theCucumberReportFileIsParsed()  {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @Then("^the test results should appear in the tool$")
    fun theTestResultsShouldAppearInTheTool() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

}