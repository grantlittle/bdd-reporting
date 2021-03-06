package org.bdd.reporting.services

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.bdd.reporting.data.*
import org.bdd.reporting.events.CucumberFeatureEvent
import org.bdd.reporting.events.EventBus
import org.bdd.reporting.web.rest.cucumber.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Service
class CucumberFeatureConsumer(@Qualifier("DbEventBus")private val eventBus : EventBus) {

    private val log : Log = LogFactory.getLog(CucumberFeatureConsumer::class.java)

    @PostConstruct
    fun start()  {
        log.info("Starting CucumberFeatureConsumer")
        eventBus.register<CucumberFeatureEvent>("cucumber-features", {onCucumberFeature(it) })
    }

    @PreDestroy
    fun stop() {
        log.info("Stopping CucumberFeatureConsumer")
    }


    fun onCucumberFeature(event : CucumberFeatureEvent) {

        if (log.isInfoEnabled) {
            log.info("Received ConsumerRecord with contents $event")
        }
        val commonFeature = map(event.feature as CucumberFeature, properties(event.properties))
        eventBus.send("common-features", event.uuid, commonFeature)
    }

    internal fun properties(strings : Set<String>?) : Set<CommonProperty> {
        if (strings == null) {
            return emptySet()
        }
        return strings.map(::CommonProperty).toSet()
    }

    fun tags(input : Set<CucumberTag>?) : Set<CommonTag> {
        input ?: return emptySet()
        return input.map { CommonTag(it.name as String, it.line) }.toSet()
    }

    private fun map(input : CucumberFeature, properties : Set<CommonProperty>) : CommonFeature {
        return CommonFeature(id = input.id ?: input.name!!,
                timestamp = input.timestamp,
                name = input.name!!,
                description = input.description,
                tags = tags(input.tags),
                properties = properties,
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
                name = it.name,
                result = result(it.result)
        ) }
    }

    private fun result(input : CucumberResult?) : String {
        if (null == input) return "UNKNOWN"
        return input.status ?: "UNKNOWN"
    }


}
