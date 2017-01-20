package org.bdd.reporting.kafka

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.concurrent.Executors

/**
 * Created by Grant Little grant@grantlittle.me
 */
open class ManagedKafkaConsumer<K, V>(val name : String,
                                      var map: Map<String, Any>,
                                      val topics : Set<String>) {

    companion object {
        private val LOG : Log = LogFactory.getLog(ManagedKafkaConsumer::class.java)
    }

    private var running = true
    private val taskExecutor = Executors.newSingleThreadExecutor()

    fun start(handler : (ConsumerRecord<K, V>) -> Unit) {
        LOG.info("($name) Starting kafka consumer")
        taskExecutor.execute {
            LOG.trace("($name) Kafka consumer")
            val consumer = KafkaConsumer<K, V>(map)
            consumer.subscribe(topics)
            while (running) {
                try {
                    LOG.trace("($name) Polling")
                    val result = consumer.poll(5000)
                    LOG.info("($name) Returned result $result")
                    result?.forEach { handler(it) }
                } catch (e : Exception) {
                    LOG.error("($name) Exception caught while polling for message", e)
                }
            }
            consumer.close()

        }
    }


    open fun stop() {
        running = false
    }


}