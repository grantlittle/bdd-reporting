package org.bdd.reporting.web.rest.controllers

import org.bdd.reporting.repository.FeatureOverviewRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

/**
 */
@RestController
@RequestMapping(value = "/api/feature-overview/1.0")
class OverviewRestController(val featureOverviewRepository: FeatureOverviewRepository) {


}

