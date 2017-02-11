package org.bdd.reporting.web.rest.cucumber

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class CucumberFeature constructor (
        var name: String? = null,
        var description: String? = null,
        var id: String? = null,
        var keyword: String? = null,
        var line: Int = 0,
        var uri: String? = null,
        var elements: Array<CucumberScenario> = emptyArray(),
        var tags: Set<CucumberTag>? = null,
        val timestamp : Date = Date()) : Serializable {

    companion object {

        private val serialVersionUID = -7801331797415036693L
    }


}
