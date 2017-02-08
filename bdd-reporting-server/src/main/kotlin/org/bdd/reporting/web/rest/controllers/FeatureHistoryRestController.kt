package org.bdd.reporting.web.rest.controllers

import org.bdd.reporting.repository.elasticsearch.FeatureHistory
import org.bdd.reporting.repository.elasticsearch.FeatureHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

/**
 */
@Service
@RequestMapping("/api/featurehistory/1.0")
class FeatureHistoryRestController(val featureHistoryRepository: FeatureHistoryRepository) {

    @GetMapping(produces = arrayOf("application/json"))
    @ResponseBody
    fun get(@RequestParam("id", required = true)id : String) : List<FeatureHistory> {
        return featureHistoryRepository.findByFeatureId(id)
    }
}