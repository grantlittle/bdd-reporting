package org.bdd.reporting.web.rest.pickles

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.bdd.reporting.events.PicklesFeatureEvent
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Grant Little grant@grantlittle.me
 */
@RestController
@RequestMapping("/api/1.0/features/pickles")
class PicklesFeatureRestController(val kafkaProducer : KafkaProducer<String, Any>) {

    private val LOG : Log = LogFactory.getLog(PicklesFeatureRestController::class.java)

    @PutMapping(consumes = arrayOf("application/json"))
    fun saveFeatures(@RequestBody root: PickleRoot) {
        if (LOG.isInfoEnabled) {
            LOG.info("Adding features data to features " + root)
        }

        kafkaProducer.send(ProducerRecord<String, Any>("pickle-features", PicklesFeatureEvent(root = root)))

    }
}