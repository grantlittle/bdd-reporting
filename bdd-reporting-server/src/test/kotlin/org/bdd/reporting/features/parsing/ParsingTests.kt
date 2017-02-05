package org.bdd.reporting.features.parsing

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith
//import org.springframework.kafka.test.rule.KafkaEmbedded


/**
 * Created by Grant Little grant@grantlittle.me
 */
@RunWith(Cucumber::class)
@CucumberOptions(features = arrayOf("src/test/resources/features/parsing"),
        glue = arrayOf(
                "org.bdd.reporting.features.parsing",
                "cucumber.api.spring"
        ),
        plugin = arrayOf("json:target/cucumber-report/ParsingTests.json"))
class ParsingTests {
//    companion object {
//
//        @ClassRule
//        @JvmField
//        var kafkaEmbedded = KafkaEmbedded(1)
//    }

}