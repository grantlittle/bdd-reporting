package org.bdd.reporting.web.rest.controllers

import org.bdd.reporting.repository.elasticsearch.FeatureOverview
import org.bdd.reporting.repository.elasticsearch.FeatureOverviewRepository
import org.springframework.web.bind.annotation.*

@Suppress("unused")
@RestController
@RequestMapping("/api/dashboard/1.0")
class DashboardController(val repository: FeatureOverviewRepository) {

    @GetMapping("/", produces = arrayOf("application/json"))
    @ResponseBody
    fun all(@RequestParam("property", required = false)property : String?) : DashboardOverview {
        if (null == property) {
            return createDashboardOverview(repository.findAll())
        } else {
            val values = property.split(":")
            return createDashboardOverview(repository.findByProperty(values[0], values[1]))
        }
    }

    internal fun createDashboardOverview(data : Iterable<FeatureOverview>) : DashboardOverview {
        val overview = DashboardOverview()
        data.forEach {
            overview.totalFeatures++
            overview.failedScenarios += it.failedScenarios
            overview.passedScenarios += it.passedScenarios
            overview.ignoredScenarios += it.ignoredScenarios
            overview.pendingScenarios += it.pendingScenarios
            overview.totalScenarios += (it.failedScenarios + it.ignoredScenarios + it.passedScenarios + it.pendingScenarios)
        }
        return overview
    }

}

class DashboardOverview(var totalFeatures : Int = 0,
                        var passedScenarios : Int = 0,
                        var failedScenarios : Int = 0,
                        var ignoredScenarios : Int = 0,
                        var pendingScenarios : Int = 0,
                        var totalScenarios : Int = 0)

