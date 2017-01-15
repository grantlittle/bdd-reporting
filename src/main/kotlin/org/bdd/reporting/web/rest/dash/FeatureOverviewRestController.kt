package org.bdd.reporting.web.rest.dash

import org.bdd.reporting.repository.FeatureOverview
import org.bdd.reporting.repository.FeatureOverviewRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Grant Little grant@grantlittle.me
 */
@RestController
@RequestMapping("/api/featureoverview/1.0")
class FeatureOverviewRestController(val repository: FeatureOverviewRepository) {

    @GetMapping
    fun get() : Iterable<FeatureOverview> {
        return repository.findAll()
    }


}