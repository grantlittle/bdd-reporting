package org.bdd.reporting.services

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.events.EventBus
import org.bdd.reporting.repository.FeatureRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Service
class CommonFeatureConsumer(@Qualifier("DbEventBus")val eventBus : EventBus,
                            val featureRepository: FeatureRepository) {

    companion object {
        private val LOG : Log = LogFactory.getLog(CommonFeatureConsumer::class.java)
    }

    @PostConstruct
    fun start()  {
        LOG.info("Starting CommonFeatureConsumer")
        eventBus.register<CommonFeature>("common-features", { onCommonFeature(it)})
    }

    @PreDestroy
    fun stop() {
        LOG.info("Stopping CommonFeatureConsumer")
    }


    fun onCommonFeature(event : CommonFeature) {
        LOG.info("Common Feature received " + event)
        featureRepository.save(event)
    }

//    fun tags(input : Set<CucumberTag>?) : Set<CommonTag> {
//        input ?: return emptySet()
//        return input.map { CommonTag(it.name as String, it.line) }.toSet()
//    }


}
