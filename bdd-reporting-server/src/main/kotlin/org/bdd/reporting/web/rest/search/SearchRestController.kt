package org.bdd.reporting.web.rest.search

import org.bdd.reporting.data.CommonFeature
import org.bdd.reporting.repository.elasticsearch.FeatureRepository
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@RestController
@RequestMapping(value = "/api/search/1.0")
class SearchRestController(val featureRepository: FeatureRepository) {

    @ResponseBody
    @GetMapping
    fun searchByName(@RequestParam(name = "name", required = false) name : String?,
                     @RequestParam(name = "tag", required = false) tag : String?,
                     @RequestParam(name = "text", required = false) text : String?) : Set<CommonFeature> {
        if (!StringUtils.isEmpty(name)) {
            return featureRepository.findByName(name!!)
        } else if (!StringUtils.isEmpty(tag)) {
            return featureRepository.findByTag(tag!!)
        } else if (!StringUtils.isEmpty(text)) {
            return featureRepository.generalSearch()
        }
        return emptySet()
    }

}
