package org.bdd.reporting.web.rest.pickles

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PickleTableArgument(@JsonProperty("HeaderRow") var headerRow: Array<String>? = null,
                               @JsonProperty("DataRows") var dataRows: Array<Array<String>>? = null)
