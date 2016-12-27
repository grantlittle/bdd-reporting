package org.bdd.reporting.data

import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
data class CommonFeature(val id : String,
                         val name : String,
                         val description : String?,
                         val labels : Set<String>,
                         val tags : Set<CommonTag>,
                         val timestamp : Date)