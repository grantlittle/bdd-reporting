package org.bdd.reporting.web.rest.nunit

import org.apache.commons.logging.LogFactory
import org.bdd.reporting.data.CommonProperty
import org.bdd.reporting.events.EventBus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayInputStream
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@RestController
@RequestMapping("/api/features/1.0/nunit")
class NUnitRestController(val eventBus : EventBus) {

    companion object {
        private val LOG = LogFactory.getLog(NUnitRestController::class.java)
    }


    @PutMapping(consumes = arrayOf(MediaType.TEXT_PLAIN_VALUE))
    fun saveFeatures(@RequestBody xml : String, @RequestHeader("BDD-Reporting-Properties", required = false)properties : String?) {
        if (LOG.isInfoEnabled) {
            LOG.info("Parsing XML from NUNit report")
        }
        val propsSet = properties?.split(",")?.toSet() ?: emptySet<String>()
        val propertiesSet = propsSet.map {
            val elements = it.split("=")
            CommonProperty(elements[0], elements[1])
        }.toSet()
        val parser = NUnitParser()
        val features = parser.parse(ByteArrayInputStream(xml.toByteArray()), properties = propertiesSet)

        features
                .forEach {
                    val id = it.id ?: UUID.randomUUID().toString()
                    LOG.info("Sending $it to event bus")
                    eventBus.send("common-features", id, it)
                }
    }

}