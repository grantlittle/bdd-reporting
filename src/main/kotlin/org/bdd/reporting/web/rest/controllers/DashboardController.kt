package org.bdd.reporting.web.rest.controllers

import org.bdd.reporting.repository.FeatureOverviewRepository
import org.bdd.reporting.web.rest.dash.DashboardOverview
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard/1.0")
class DashboardController(val repository: FeatureOverviewRepository) {

    @GetMapping("/", produces = arrayOf("application/json"))
    @ResponseBody
    fun all() : DashboardOverview {
        val overview = DashboardOverview()
        repository.findAll().forEach {
            overview.failedScenarios += it.failedScenarios
            overview.passedScenarios += it.passedScenarios
            overview.ignoredScenarios += it.ignoredScenarios
            overview.pendingScenarios += it.pendingScenarios
            overview.totalScenarios += (it.failedScenarios + it.ignoredScenarios + it.passedScenarios + it.pendingScenarios)
        }
        return overview
    }

}

