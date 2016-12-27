package org.bdd.reporting.web.rest.pickles

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by Grant Little grant@grantlittle.me
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PickleFeatureInfo(@JsonProperty("RelativeFolder")var relativeFolder : String?,
                             @JsonProperty("Feature") var feature: PickleFeature? ) {
}