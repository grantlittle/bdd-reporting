package org.bdd.reporting.web.rest.search

import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.repository.FeatureRepository
import org.springframework.web.bind.annotation.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@RestController
@RequestMapping(value = "/api/1.0/search")
class SearchRestController(val featureRepository: FeatureRepository) {

    @ResponseBody
    @GetMapping
    fun getFeature(@RequestParam(name = "name") name : String) : Set<CommonFeature>{
        return featureRepository.findByName(name)
    }

}
