package org.bdd.reporting.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.data.CommonTag
import org.bdd.reporting.events.CucumberFeatureEvent
import org.bdd.reporting.web.rest.cucumber.CucumberTag
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Service
class CucumberFeatureConsumer(@Qualifier("CucumberFeatureManagedConsumer")private val consumer : ManagedKafkaConsumer<String, CucumberFeatureEvent>,
                              private val producer : KafkaProducer<String, Any>) {

    @PostConstruct
    fun start()  {
        consumer.handler { onCucumberFeature(it) }
    }

    @PreDestroy
    fun stop() {
        consumer.stop()
    }


    fun onCucumberFeature(record : ConsumerRecord<String, CucumberFeatureEvent>) {
        val event = record.value()


        val commonFeature = CommonFeature(id = event!!.feature!!.id ?: event.feature?.name!!,
                timestamp = event.feature!!.timestamp,
                name = event.feature.name!!,
                description = event.feature.description,
                labels = event.feature.labels ?: emptySet(),
                tags = tags(event.feature.tags))

        producer.send(ProducerRecord<String, Any>("common-features", event.uuid, commonFeature))

    }

    fun tags(input : Set<CucumberTag>?) : Set<CommonTag> {
        input ?: return emptySet()
        return input.map { CommonTag(it.name as String, it.line) }.toSet()
    }


}
