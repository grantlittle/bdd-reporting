package org.bdd.reporting.features.search

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.ClassRule
import org.junit.runner.RunWith
import org.springframework.kafka.test.rule.KafkaEmbedded

/**
 * Created by Grant Little grant@grantlittle.me
 */
@RunWith(Cucumber::class)
@CucumberOptions(features = arrayOf("src/test/resources/features/search"),
        glue = arrayOf("org.bdd.reporting.features.search", "cucumber.api.spring"))
class SearchTests {
    companion object {
        @ClassRule
        @JvmField
        var kafkaEmbedded = KafkaEmbedded(1)
    }

}