package org.bdd.reporting.kafka

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.data.CommonScenario
import org.bdd.reporting.data.CommonStep
import org.bdd.reporting.data.CommonTag
import org.bdd.reporting.events.CucumberFeatureEvent
import org.bdd.reporting.web.rest.cucumber.*
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

    private val log : Log = LogFactory.getLog(CucumberFeatureConsumer::class.java)

    @PostConstruct
    fun start()  {
        consumer.start { onCucumberFeature(it) }
    }

    @PreDestroy
    fun stop() {
        consumer.stop()
    }


    fun onCucumberFeature(record : ConsumerRecord<String, CucumberFeatureEvent>) {

        if (log.isInfoEnabled) {
            log.info("Received ConsumerRecord with contents ${record.value()}")
        }
        val event = record.value()

        val commonFeature = map(event!!.feature as CucumberFeature)
        producer.send(ProducerRecord<String, Any>("common-features", event.uuid, commonFeature))

    }

    fun tags(input : Set<CucumberTag>?) : Set<CommonTag> {
        input ?: return emptySet()
        return input.map { CommonTag(it.name as String, it.line) }.toSet()
    }

    private fun map(input : CucumberFeature) : CommonFeature {
        return CommonFeature(id = input.id ?: input.name!!,
                timestamp = input.timestamp,
                name = input.name!!,
                description = input.description,
                labels = input.labels ?: emptySet(),
                tags = tags(input.tags),
                scenarios = scenarios(input.elements))
    }

    private fun scenarios(input : Array<CucumberScenario>) : List<CommonScenario> {
        return input.map { CommonScenario(
                id = it.id,
                name = it.name,
                description = it.description,
                tags = tags(it.tags),
                type = it.type,
                keyword = it.keyword,
                line = it.line,
                steps = steps(it.steps ?: mutableListOf())
        ) }
    }

    private fun steps(input : List<CucumberStep>) : List<CommonStep> {
        return input.map { CommonStep(
                line = it.line,
                keyword = it.keyword,
                result = result(it.result)
        ) }
    }

    private fun result(input : CucumberResult?) : String {
        if (null == input) return "UNKNOWN"
        return input.status ?: "UNKNOWN"
    }


}
