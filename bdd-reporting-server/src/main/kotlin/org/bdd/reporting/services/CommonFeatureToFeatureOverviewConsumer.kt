package org.bdd.reporting.services

import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.data.CommonScenario
import org.bdd.reporting.data.CommonStep
import org.bdd.reporting.events.EventBus
import org.bdd.reporting.repository.elasticsearch.FeatureOverview
import org.bdd.reporting.repository.elasticsearch.FeatureOverviewRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Suppress("unused")
@Service
open class CommonFeatureToFeatureOverviewConsumer(@Qualifier("DbEventBus")val eventBus: EventBus,
                                                  val featureOverviewRepository: FeatureOverviewRepository) {

    @PostConstruct
    fun start()  {
        eventBus.register<CommonFeature>("common-features", { handle(it)})

    }

    @PreDestroy
    fun stop() {
    }

    internal fun handle(commonFeature : CommonFeature) {
        val featureOverview = toFeatureOverview(commonFeature)
        featureOverviewRepository.index(featureOverview)
    }

    internal fun toFeatureOverview(commonFeature: CommonFeature) : FeatureOverview {
        val result = scenarios(commonFeature.scenarios)
        return FeatureOverview(
                id = commonFeature.id,
                timestamp = commonFeature.timestamp,
                description = commonFeature.description,
                name = commonFeature.name,
                passedScenarios = result.first["passed"] ?: 0,
                failedScenarios = result.first["failed"] ?: 0,
                pendingScenarios = result.first["pending"] ?: 0,
                ignoredScenarios = result.first["ignored"] ?: 0,
                totalScenarios = result.first["total"] ?: 0,
                passedSteps = result.second["passed"] ?: 0,
                failedSteps = result.second["failed"] ?: 0,
                pendingSteps = result.second["pending"] ?: 0,
                ignoredSteps = result.second["ignored"] ?: 0,
                totalSteps = result.second["total"] ?: 0,
                overallStatus = determineOverallStatus(result.first),
                labels = commonFeature.labels ?: mutableSetOf(),
                tags = commonFeature.tags
                )
    }

    internal fun scenarios(scenarios : List<CommonScenario>) : Pair<Map<String, Int>, Map<String, Int>> {
        val stepStats = mutableMapOf<String, Int>()
        val scenarioStats = mutableMapOf<String, Int>()
        scenarios.map {
            val output = steps(it.steps)
            incrementStepStats(output, stepStats)
            increment("total", scenarioStats)
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
            increment("total", map)
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

    internal fun determineOverallStatus(data : Map<String, Int>) : String {
        if (data["failed"] ?: 0 > 0) {
            return "failed"
        }
        else if (data["pending"] ?: 0 > 0) {
            return "pending"
        }
        else if (data["passed"] ?: 0 > 0) {
            return "passed"
        }
        else if (data["ignored"] ?: 0 > 0) {
            return "ignored"
        }
        throw IllegalStateException("Could not determine over state with data $data")
    }
}