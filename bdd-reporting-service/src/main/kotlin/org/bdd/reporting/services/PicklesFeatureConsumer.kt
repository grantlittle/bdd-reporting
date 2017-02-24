package org.bdd.reporting.services

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.bdd.reporting.data.*
import org.bdd.reporting.events.EventBus
import org.bdd.reporting.events.PicklesFeatureEvent
import org.bdd.reporting.web.rest.pickles.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Service
class PicklesFeatureConsumer(@Qualifier("DbEventBus")private val eventBus : EventBus) {

    private val log : Log = LogFactory.getLog(PicklesFeatureConsumer::class.java)

    @PostConstruct
    fun start()  {
        log.info("Starting CucumberFeatureConsumer")
        eventBus.register<PicklesFeatureEvent>("pickle-features", { onPickleFeature(it) })
    }

    @PreDestroy
    fun stop() {
        log.info("Stopping CucumberFeatureConsumer")
    }


    fun onPickleFeature(event : PicklesFeatureEvent) {

        if (log.isInfoEnabled) {
            log.info("Received ConsumerRecord with contents $event")
        }
        val commonFeatures = mapPickleFeatures(event.root?.features!!, properties(event.properties))
        commonFeatures.forEach {
            eventBus.send("common-features", event.uuid, it)
        }
    }

    internal fun properties(strings : Set<String>?) : Set<CommonProperty> {
        if (strings == null) {
            return emptySet()
        }
        return strings.map(::CommonProperty).toSet()
    }


    internal fun mapPickleFeatures(pickleFeatures : List<PickleFeatureInfo>, properties : Set<CommonProperty>) : List<CommonFeature> {
        return pickleFeatures.map { mapPickleFeature(it, properties) }
    }

    internal fun mapPickleFeature(pickleFeature : PickleFeatureInfo, properties : Set<CommonProperty>) : CommonFeature {
        val pFeature = pickleFeature.feature
        val tags = pFeature?.tags?.map { mapTag(it) }?.toSet() ?: emptySet()

        val cFeature = CommonFeature(tags = tags, properties = properties)
        cFeature.timestamp = Date()
        cFeature.name = pFeature?.name
        cFeature.description = pFeature?.description
        cFeature.id = toId(pFeature?.name)
        val cScenarios = mutableListOf<CommonScenario>()
        pFeature?.scenarios?.forEach {
            cScenarios.addAll(mapPickleScenario(it))
        }
        cFeature.scenarios = cScenarios
        return cFeature
    }

    internal fun mapPickleScenario(pScenario : PickleScenario) : List<CommonScenario> {
        if (pScenario.examples?.isNotEmpty() ?: false) {
            val exampleScenarios = mutableListOf<CommonScenario>()
            pScenario.examples?.forEach { exampleScenarios.addAll(mapExample(it, pScenario)) }
            return exampleScenarios

        } else {
            val cScenario = CommonScenario()
            cScenario.id = toId(pScenario.name)
            cScenario.description = pScenario.description
            cScenario.name = pScenario.name
            cScenario.tags = pScenario.tags?.map { mapTag(it) }?.toSet() ?: emptySet()
            cScenario.type = "Scenario"
            cScenario.keyword = "Scenario"
            val cResult = mapResult(pScenario.result!!)
            cScenario.steps = pScenario.steps?.map {
                mapStep(it, cResult)
            } ?: emptyList()
            return listOf(cScenario)
        }
    }

    internal fun mapStep(pStep : PickleStep, result : String) : CommonStep {
        val cStep = CommonStep(line = null)
        cStep.name = pStep.name
        cStep.result = result
        cStep.keyword = pStep.keyword
        return cStep
    }

    internal fun mapExample(pExample : PickleExample, pScenario : PickleScenario) : List<CommonScenario> {
        val dataRows = pExample.tableArgument?.dataRows
        if (dataRows?.isEmpty() ?: true) {
            return emptyList()
        }
        val scenarios = mutableListOf<CommonScenario>()
        val outcome = mapResult(pScenario.result!!)
        dataRows!!.forEachIndexed { i, s ->
            val cScenario = CommonScenario(line = null)
            cScenario.id = toId("${pScenario.name}_${mapStringParams(s)}")
            cScenario.name = pScenario.name
            cScenario.description = pScenario.description
            cScenario.keyword = "Scenario"
            cScenario.type = "Scenario"
            cScenario.tags = pScenario.tags?.map { mapTag(it) }?.toSet() ?: emptySet()
            val dataRow = pExample.tableArgument!!.dataRows!![i]
            val parameterMap = createParameterMap(pExample.tableArgument!!.headerRow!!, dataRow)
            cScenario.steps = pScenario.steps?.map { mapScenarioOutlineStep(it, parameterMap, outcome) } ?: emptyList()
            scenarios.add(cScenario)
        }
        return scenarios
    }

    internal fun mapStringParams(params : Array<String>) : String {
        val joiner = StringJoiner("_")
        params.forEach { joiner.add(it) }
        return joiner.toString()
    }

    internal fun mapScenarioOutlineStep(pStep : PickleStep, parameters : Map<String, String>, result : String) : CommonStep {
        val cStep = CommonStep(line = null)
        var actualName = pStep.name
        parameters.forEach {
            if (pStep.name?.contains("<${it.key}>") ?: false) {
                actualName = pStep.name!!.replace("<${it.key}>", it.value)
            }

        }
        cStep.name = actualName
        cStep.keyword = pStep.keyword
        cStep.result = result
        return cStep
    }

    internal fun createParameterMap(headers : Array<String>, dataRow : Array<String>) : Map<String, String> {
        val result = mutableMapOf<String, String>()
        headers.forEachIndexed { i, s ->  result.put(headers[i], dataRow[1]) }
        return result
    }

    internal fun mapResult(pResult : PickleResult) : String {
        if (pResult.wasExecuted!! && pResult.wasSuccessful!!) {
            return "passed"
        } else if (pResult.wasExecuted!! && !pResult.wasSuccessful!!) {
            return "failed"
        } else  {
            return "pending"
        }
    }

    internal fun mapTag(tag : String) : CommonTag {
        return CommonTag(tag)
    }

    internal fun toId(input : String?): String? {
        if (null == input) return null

        return input.toLowerCase().replace(" ", "_")
    }

//    internal fun properties(strings : Set<String>?) : Set<CommonProperty> {
//        if (strings == null) {
//            return emptySet()
//        }
//        return strings.map(::CommonProperty).toSet()
//    }
//
//    fun tags(input : Set<CucumberTag>?) : Set<CommonTag> {
//        input ?: return emptySet()
//        return input.map { CommonTag(it.name as String, it.line) }.toSet()
//    }
//
//    private fun map(input : CucumberFeature, properties : Set<CommonProperty>) : CommonFeature {
//        return CommonFeature(id = input.id ?: input.name!!,
//                timestamp = input.timestamp,
//                name = input.name!!,
//                description = input.description,
//                tags = tags(input.tags),
//                properties = properties,
//                scenarios = scenarios(input.elements))
//    }
//
//    private fun scenarios(input : Array<CucumberScenario>) : List<CommonScenario> {
//        return input.map { CommonScenario(
//                id = it.id,
//                name = it.name,
//                description = it.description,
//                tags = tags(it.tags),
//                type = it.type,
//                keyword = it.keyword,
//                line = it.line,
//                steps = steps(it.steps ?: mutableListOf())
//        ) }
//    }
//
//    private fun steps(input : List<CucumberStep>) : List<CommonStep> {
//        return input.map { CommonStep(
//                line = it.line,
//                keyword = it.keyword,
//                name = it.name,
//                result = result(it.result)
//        ) }
//    }
//
//    private fun result(input : CucumberResult?) : String {
//        if (null == input) return "UNKNOWN"
//        return input.status ?: "UNKNOWN"
//    }


}
