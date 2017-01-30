package org.bdd.reporting.web.rest.cucumber

import java.io.Serializable

/**
 * Created by Grant Little grant@grantlittle.me
 */
data class CucumberResult(
        var status: String?,
        var duration: Long = 0) : Serializable {

    constructor() : this(null, 0)

    companion object {

        private val serialVersionUID = -6073087009395171764L
    }
}
