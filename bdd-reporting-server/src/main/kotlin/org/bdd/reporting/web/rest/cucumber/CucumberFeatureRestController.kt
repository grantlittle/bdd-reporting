package org.bdd.reporting.web.rest.cucumber

import org.apache.commons.logging.LogFactory
import org.bdd.reporting.events.CucumberFeatureEvent
import org.bdd.reporting.events.EventBus
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Suppress("unused")
@RestController
@RequestMapping("/api/1.0/features/cucumber")
class CucumberFeatureRestController(val eventBus : EventBus) {

    init {
        if (LOG.isInfoEnabled) LOG.info("CucumberFeatureRestController starting")
    }

    companion object {
        private val LOG = LogFactory.getLog(CucumberFeatureRestController::class.java)
    }


    @PutMapping(consumes = arrayOf("application/json"))
    fun saveFeatures(@RequestBody features: List<CucumberFeature>) {
        if (LOG.isErrorEnabled) {
            LOG.info("Adding features data to features " + features)
        }

        features
            .forEach {
                val id = it.id ?: UUID.randomUUID().toString()
                LOG.info("Sending $it to event bus")
                eventBus.send("cucumber-features", id, CucumberFeatureEvent(feature = it))
            }

    }


    @PutMapping(consumes = arrayOf("application/json"), value = "/{labels}")
    fun saveFeatures(@RequestBody features: List<CucumberFeature>, @PathVariable("labels") labelsAsString: String) {
        if (LOG.isInfoEnabled) {
            LOG.info("Adding features data to features " + features)
        }

        features
                .forEach {
                    val id = it.id ?: UUID.randomUUID().toString()
                    LOG.info("Sending $it to event bus")
                    eventBus.send("cucumber-features", id, CucumberFeatureEvent(feature = it, labels = labelsAsString))
                }
    }


}
