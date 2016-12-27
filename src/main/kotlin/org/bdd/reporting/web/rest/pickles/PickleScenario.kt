package org.bdd.reporting.web.rest.pickles

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Grant Little grant@grantlittle.me
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PickleScenario(@JsonProperty("Name") var name: String? = null,
                          @JsonProperty("Description") var description : String? = null,
                          @JsonProperty("Steps") var steps : List<PickleStep>? = emptyList(),
                          @JsonProperty("Tags") var tags : Set<String>? = emptySet(),
                          @JsonProperty("Result") var result : PickleResult? = null,
                          @JsonProperty("labels")var labels: Set<String>? = null,
                          @JsonProperty("Examples")var examples: List<PickleExample>? = null)

