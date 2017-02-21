package org.bdd.reporting.web.rest.pickles

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.bdd.reporting.events.EventBus
import org.bdd.reporting.events.PicklesFeatureEvent
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@RestController
@RequestMapping("/api/features/1.0/pickles")
class PicklesFeatureRestController(val eventBus: EventBus) {

    private val LOG : Log = LogFactory.getLog(PicklesFeatureRestController::class.java)

    @PutMapping(consumes = arrayOf("application/json"))
    fun saveFeatures(@RequestBody root: PickleRoot, @RequestHeader("BDD-Reporting-Properties", required = false)properties : String?) {
        if (LOG.isInfoEnabled) {
            LOG.info("Adding features data to features " + root)
        }
        val propsSet = properties?.split(",")?.toSet() ?: emptySet<String>()

        val id = UUID.randomUUID().toString()
        eventBus.send("pickle-features", PicklesFeatureEvent(root = root, uuid = id, properties = propsSet))
    }
}