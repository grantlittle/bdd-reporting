package org.bdd.reporting.kafka

import org.apache.kafka.common.serialization.StringDeserializer
import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.data.CommonScenario
import org.bdd.reporting.data.CommonStep
import org.bdd.reporting.repository.FeatureOverview
import org.bdd.reporting.repository.FeatureOverviewRepository
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Suppress("unused")
@Service
open class CommonFeatureToFeatureOverviewConsumer(val kafkaSettings: KafkaSettings,
                                                  val featureOverviewRepository: FeatureOverviewRepository) {

    var consumer : ManagedKafkaConsumer<String, CommonFeature>? = null

    @PostConstruct
    fun start()  {
        val props = mutableMapOf(
                Pair("bootstrap.servers", kafkaSettings.brokers),
                Pair("key.deserializer", StringDeserializer::class.java.name),
                Pair("value.deserializer", CommonFeatureJsonDeserializer::class.java.name),
                Pair("group.id", "common->featureOverview")
        )
        consumer = ManagedKafkaConsumer(props, setOf("common-features"))
        consumer?.start { handle(it.value()) }

    }

    @PreDestroy
    fun stop() {
        consumer?.stop()
    }

    internal fun handle(commonFeature : CommonFeature) {
        val featureOverview = toFeatureOverview(commonFeature)
        featureOverviewRepository.index(featureOverview)
    }

    internal fun toFeatureOverview(commonFeature: CommonFeature) : FeatureOverview {
        val result = scenarios(commonFeature.scenarios)
        return FeatureOverview(
                id = commonFeature.id,
                description = commonFeature.description,
                name = commonFeature.name,
                passedScenarios = result.first["passed"] ?: 0,
                failedScenarios = result.first["failed"] ?: 0,
                pendingScenarios = result.first["pending"] ?: 0,
                ignoredScenarios = result.first["ignored"] ?: 0,
                passedSteps = result.second["passed"] ?: 0,
                failedSteps = result.second["failed"] ?: 0,
                pendingSteps = result.second["pending"] ?: 0,
                ignoredSteps = result.second["ignored"] ?: 0
                )
    }

    internal fun scenarios(scenarios : List<CommonScenario>) : Pair<Map<String, Int>, Map<String, Int>> {
        val stepStats = mutableMapOf<String, Int>()
        val scenarioStats = mutableMapOf<String, Int>()
        scenarios.map {
            val output = steps(it.steps)
            incrementStepStats(output, stepStats)
            when (getScenarioStatus(output)) {
                "passed" -> increment("passed", scenarioStats)
                "failed" -> increment("failed", scenarioStats)
                "pending" -> increment("pending", scenarioStats)
                "ignored" -> increment("ignored", scenarioStats)
            }

        }
        return Pair(scenarioStats, stepStats)
    }

    internal fun incrementStepStats(stepStats : Map<String, Int>, overall : MutableMap<String, Int>) {
        for ((k, v) in stepStats) {
            var count = stepStats[k] ?: 0
            count += v
            overall.put(k, count)
        }
    }

    internal fun getScenarioStatus(stepStats : Map<String, Int>) : String {
        if (stepStats["failed"] ?: 0 > 0) return "failed"
        if (stepStats["pending"] ?: 0 > 0) return "pending"
        if (stepStats["ignored"] ?: 0 > 0) return "ignored"
        return "passed"
    }

    internal fun steps(steps : List<CommonStep>) : Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        steps.forEach {
            when (it.result) {
                "passed" -> increment("passed", map)
                "failed" -> increment("failed", map)
                "pending" -> increment("pending", map)
                "ignored" -> increment("ignored", map)
            }
        }
        return map
    }

    internal fun increment(name : String, map : MutableMap<String, Int>) {
        val count  = if (map[name] != null) map[name] as Int else 0
        map[name] = count + 1

    }
}