package org.bdd.reporting.web.rest.controllers

import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.repository.elasticsearch.FeatureRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Grant Little grant@grantlittle.me
 */
@RestController
@RequestMapping("/api/feature/1.0/")
class FeatureRestController(val featureRepository: FeatureRepository) {

    @GetMapping(value = "{id}")
    fun get(@PathVariable("id") id: String) : CommonFeature {
        return featureRepository.findOne(id)
    }
}