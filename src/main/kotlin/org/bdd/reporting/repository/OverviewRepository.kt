package org.bdd.reporting.repository

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Repository
interface FeatureOverviewRepository : ElasticsearchRepository<FeatureOverview, String> {

}

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "feature_overviews")
open class FeatureOverview(@Id val id : String? = null,
                           val name : String? = null,
                           val description : String? = null,
                           val passedScenarios: Int = 0,
                           val failedScenarios: Int = 0,
                           val pendingScenarios: Int = 0,
                           val ignoredScenarios: Int = 0,
                           val passedSteps: Int = 0,
                           val failedSteps: Int = 0,
                           val pendingSteps: Int = 0,
                           val ignoredSteps: Int = 0)