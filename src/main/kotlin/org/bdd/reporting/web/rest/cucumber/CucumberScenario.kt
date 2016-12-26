package org.bdd.reporting.web.rest.cucumber

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

/**
 * Created by Grant Little grant@grantlittle.me
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class CucumberScenario(
        var id: String? = null,
        var name: String? = null,
        var line: Int = 0,
        var description: String? = null,
        var type: String? = null,
        var keyword: String? = null,
        var steps: List<CucumberStep>? = null,
        var tags: Set<CucumberTag>? = null,
        var labels: Set<String>? = null
) : Serializable {

    companion object {

        private val serialVersionUID = -6213078594895036515L
    }
}
