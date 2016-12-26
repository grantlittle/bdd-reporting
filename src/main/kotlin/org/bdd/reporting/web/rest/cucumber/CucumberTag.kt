package org.bdd.reporting.web.rest.cucumber

import java.io.Serializable

/**
 * Created by Grant Little grant@grantlittle.me
 */
data class CucumberTag constructor(var line: Int? = 0,
                             var name: String? = null
): Serializable {

    constructor() : this(null, null)

    companion object {

        private val serialVersionUID = 7130205985528992287L
    }

}
