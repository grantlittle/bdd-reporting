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
    fun saveFeatures(@RequestBody features: List<CucumberFeature>, @RequestHeader("BDD-Reporting-Properties", required = false)properties : String?) {
        if (LOG.isInfoEnabled) {
            LOG.info("Adding features data to features " + features)
        }

        features
                .forEach {
                    val id = it.id ?: UUID.randomUUID().toString()
                    LOG.info("Sending $it to event bus")
                    val propsSet = properties?.split(",")?.toSet() ?: emptySet<String>()
                    eventBus.send("cucumber-features", id, CucumberFeatureEvent(feature = it, properties = propsSet))
                }
    }


}
