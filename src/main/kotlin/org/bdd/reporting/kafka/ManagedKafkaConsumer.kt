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

    open fun handler(handler : (ConsumerRecord<K, V>) -> Unit) {
        taskExecutor.execute {
            LOG.info("KafKa consumer")
            val consumer = KafkaConsumer<K, V>(map)
            consumer.subscribe(topics)
            while (running) {
                try {
                    consumer.poll(10000)?.forEach { handler(it) }
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