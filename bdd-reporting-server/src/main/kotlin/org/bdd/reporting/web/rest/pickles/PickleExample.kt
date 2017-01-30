package org.bdd.reporting.web.rest.pickles

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PickleExample(@JsonProperty("Name") var name : String? = null,
                         @JsonProperty("TableArgument") var tableArgument : PickleTableArgument? = null)