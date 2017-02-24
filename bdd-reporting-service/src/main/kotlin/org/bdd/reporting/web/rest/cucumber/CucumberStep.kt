package org.bdd.reporting.web.rest.cucumber

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

/**
 * Created by Grant Little grant@grantlittle.me
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class CucumberStep(
        var line: Int? = 0,
        var name: String? = null,
        var keyword: String? = null,
        var match: CucumberMatch? = null,
        var result: CucumberResult? = null): Serializable {

    companion object {

        private val serialVersionUID = -9062270948452793011L
    }
}
