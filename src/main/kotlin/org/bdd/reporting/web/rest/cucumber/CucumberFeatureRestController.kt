package org.bdd.reporting.web.rest.cucumber

import org.apache.commons.logging.LogFactory
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.bdd.reporting.events.CucumberFeatureEvent
import org.springframework.web.bind.annotation.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Suppress("unused")
@RestController
@RequestMapping("/api/1.0/features/cucumber")
class CucumberFeatureRestController(val kafkaProducer : KafkaProducer<String, Any>) {

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
                .map {
                    val id = it.id ?: "unknown".hashCode()
                    ProducerRecord<String, Any>("cucumber-features", id.toString(), CucumberFeatureEvent(feature = it))
                }
                .forEach { LOG.info("Sending ${it.value()} to kafkaProducer"); kafkaProducer.send(it) }

    }


    @PutMapping(consumes = arrayOf("application/json"), value = "/{labels}")
    fun saveFeatures(@RequestBody features: List<CucumberFeature>, @PathVariable("labels") labels: String) {
        if (LOG.isInfoEnabled) {
            LOG.info("Adding features data to features " + features)
        }

//        features
//                .map { ProducerRecord<String, Any>("cucumber-features", it.id, CucumberFeatureEvent(labels = labels, feature = it)) }
//                .forEach { producer.send(it) }
    }


}
