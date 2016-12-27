package org.bdd.reporting.events

import org.bdd.reporting.web.rest.cucumber.CucumberFeature
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
data class CucumberFeatureEvent(val feature: CucumberFeature? = null, val labels : String? = null, val uuid : String = UUID.randomUUID().toString()) {
}