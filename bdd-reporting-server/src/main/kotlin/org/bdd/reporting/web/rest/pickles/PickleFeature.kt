package org.bdd.reporting.web.rest.pickles

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Grant Little grant@grantlittle.me
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PickleFeature(@JsonProperty("Name") var name: String? = null,
                         @JsonProperty("Description") var description: String? = null,
                         @JsonProperty("FeatureElements") var scenarios: List<PickleScenario>? = emptyList(),
                         @JsonProperty("Result")var result: PickleResult? = null,
                         @JsonProperty("Tags")var tags: Set<String>? = emptySet(),
                         @JsonProperty("properties")var labels: Set<String>? = null)