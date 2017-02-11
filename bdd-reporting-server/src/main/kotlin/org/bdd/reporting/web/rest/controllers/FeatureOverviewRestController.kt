package org.bdd.reporting.web.rest.controllers

import org.bdd.reporting.repository.elasticsearch.FeatureOverview
import org.bdd.reporting.repository.elasticsearch.FeatureOverviewRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

/**
 */
@Service
@RequestMapping("/api/featureoverviews/1.0")
class FeatureOverviewRestController(val featureOverviewRepository: FeatureOverviewRepository) {

    @GetMapping("/", produces = arrayOf("application/json"))
    @ResponseBody
    fun get(@RequestParam("properties")labels : String, @RequestParam("tags") tags :String) : List<FeatureOverview> {
        return featureOverviewRepository.findAll().toList()
    }
}