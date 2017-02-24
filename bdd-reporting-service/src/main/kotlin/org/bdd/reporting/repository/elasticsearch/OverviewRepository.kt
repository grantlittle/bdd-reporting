package org.bdd.reporting.repository.elasticsearch

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.bdd.reporting.data.CommonProperty
import org.bdd.reporting.data.CommonTag
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Repository
interface FeatureOverviewRepository : ElasticsearchRepository<FeatureOverview, String> {

    @Query("""
            {
              "query": {
                  "bool": {
                      "must": [
                        { "match": { "properties.key": "?0" } },
                        { "match": { "properties.value": "?1" } }
                      ]
                  }
              }
            }    """)
    fun findByProperty(propertyName : String, propertyValue : String) : Iterable<FeatureOverview>
}

@Repository
interface FeatureHistoryRepository : ElasticsearchRepository<FeatureHistory, String> {

//    @Query("""
//        {
//          "query": {
//              "bool": {
//                  "must": [
//                    { "match": { "featureId": "?0" } }
//                  ]
//              }
//          }
//        }
//    """)
    fun findByFeatureId(id : String) : List<FeatureHistory>

}


@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "feature_overviews")
open class FeatureOverview(@Id val id : String? = null,
                           val name : String? = null,
                           val timestamp : Date? = null,
                           val description : String? = null,
                           val passedScenarios: Int = 0,
                           val failedScenarios: Int = 0,
                           val pendingScenarios: Int = 0,
                           val ignoredScenarios: Int = 0,
                           val totalScenarios: Int = 0,
                           val passedSteps: Int = 0,
                           val failedSteps: Int = 0,
                           val pendingSteps: Int = 0,
                           val ignoredSteps: Int = 0,
                           val totalSteps: Int = 0,
                           val overallStatus: String? = null,
                           val tags : Set<CommonTag> = mutableSetOf(),
                           val properties : Set<CommonProperty> = mutableSetOf())

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "feature_history")
open class FeatureHistory(@Id val id : String? = null,
                          val featureId : String? = null,
                          val name : String? = null,
                          val timestamp : Date? = null,
                          val description : String? = null,
                          val passedScenarios: Int = 0,
                          val failedScenarios: Int = 0,
                          val pendingScenarios: Int = 0,
                          val ignoredScenarios: Int = 0,
                          val totalScenarios: Int = 0,
                          val passedSteps: Int = 0,
                          val failedSteps: Int = 0,
                          val pendingSteps: Int = 0,
                          val ignoredSteps: Int = 0,
                          val totalSteps: Int = 0,
                          val overallStatus: String? = null,
                          val tags : Set<CommonTag> = mutableSetOf(),
                          val properties : Set<CommonProperty> = mutableSetOf())
