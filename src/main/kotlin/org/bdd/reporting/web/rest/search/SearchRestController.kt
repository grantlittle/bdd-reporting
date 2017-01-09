package org.bdd.reporting.web.rest.search

import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.repository.AdvancedRepository
import org.bdd.reporting.repository.FeatureRepository
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@RestController
@RequestMapping(value = "/api/1.0/search")
class SearchRestController(val featureRepository: FeatureRepository,
                           val advancedRepository: AdvancedRepository) {

    @ResponseBody
    @GetMapping
    fun searchByName(@RequestParam(name = "name", required = false) name : String?,
                     @RequestParam(name = "tag", required = false) tag : String?) : Set<CommonFeature>{
        if (!StringUtils.isEmpty(name)) {
            return featureRepository.findByName(name!!)
        } else if (!StringUtils.isEmpty(tag)) {
            return featureRepository.findByTag(tag!!)
        }
        return emptySet()
    }

}
