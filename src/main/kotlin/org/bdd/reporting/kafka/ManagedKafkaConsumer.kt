package org.bdd.reporting.kafka

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.concurrent.Executors

/**
 * Created by Grant Little grant@grantlittle.me
 */
open class ManagedKafkaConsumer<K, V>(var map: Map<String, Any>,
                                      val topics : Set<String>) {

    private val LOG : Log = LogFactory.getLog(ManagedKafkaConsumer::class.java)

    private var running = true
    private val taskExecutor = Executors.newSingleThreadExecutor()

    fun start(handler : (ConsumerRecord<K, V>) -> Unit) {
        LOG.info("Starting kafka consumer")
        taskExecutor.execute {
            LOG.info("Kafka consumer")
            val consumer = KafkaConsumer<K, V>(map)
            consumer.subscribe(topics)
            while (running) {
                try {
                    LOG.info("Polling")
                    val result = consumer.poll(5000)
                    LOG.info("Returned result $result")
                    result?.forEach { handler(it) }
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
            consumer.close()

        }
    }


    open fun stop() {
        running = false
    }


}