package org.bdd.reporting.events

import org.bdd.reporting.web.rest.pickles.PickleRoot
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
data class PicklesFeatureEvent(val root: PickleRoot? = null, val properties : Set<String>? = null, val uuid : String = UUID.randomUUID().toString())