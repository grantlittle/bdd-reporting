package org.bdd.reporting.web.rest.dash

import org.bdd.reporting.repository.elasticsearch.FeatureOverview
import org.bdd.reporting.repository.elasticsearch.FeatureOverviewRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Grant Little grant@grantlittle.me
 */
@RestController
@RequestMapping("/api/featureoverview/1.0")
class FeatureOverviewRestController(val repository: FeatureOverviewRepository) {

//    @GetMapping("/", produces = arrayOf("application/json"))
//    @ResponseBody
//    fun all() : DashboardOverview {
//        val overview = DashboardOverview()
//        repository.findAll().forEach {
//            overview.failedScenarios += it.failedScenarios
//            overview.passedScenarios += it.passedScenarios
//            overview.ignoredScenarios += it.ignoredScenarios
//            overview.pendingScenarios += it.pendingScenarios
//            overview.totalScenarios += (it.failedScenarios + it.ignoredScenarios + it.passedScenarios + it.pendingScenarios)
//        }
//        return overview
//    }

    @GetMapping
    fun get() : Iterable<FeatureOverview> {
        return repository.findAll()
    }


}

class DashboardOverview(var passedScenarios : Int = 0,
                        var failedScenarios : Int = 0,
                        var ignoredScenarios : Int = 0,
                        var pendingScenarios : Int = 0,
                        var totalScenarios : Int = 0)
