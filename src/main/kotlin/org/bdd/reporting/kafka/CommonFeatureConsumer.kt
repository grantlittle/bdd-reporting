package org.bdd.reporting.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.data.CommonTag
import org.bdd.reporting.repository.FeatureRepository
import org.bdd.reporting.web.rest.cucumber.CucumberTag
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Service
class CommonFeatureConsumer(@Qualifier("CommonFeatureManagedConsumer")private val consumer : ManagedKafkaConsumer<String, CommonFeature>,
                            val featureRepository: FeatureRepository) {

    @PostConstruct
    fun start()  {
        consumer.start { onCommonFeature(it) }
    }

    @PreDestroy
    fun stop() {
        consumer.stop()
    }


    fun onCommonFeature(record : ConsumerRecord<String, CommonFeature>) {
        val event = record.value()
        featureRepository.save(event)

    }

    fun tags(input : Set<CucumberTag>?) : Set<CommonTag> {
        input ?: return emptySet()
        return input.map { CommonTag(it.name as String, it.line) }.toSet()
    }


}
