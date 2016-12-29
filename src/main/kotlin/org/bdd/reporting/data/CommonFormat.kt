package org.bdd.reporting.data

import org.springframework.data.elasticsearch.annotations.Document
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Document(indexName = "features")
data class CommonFeature(var id : String? = null,
                         var name : String? = null,
                         var description : String? = "",
                         val labels : Set<String>? = mutableSetOf(),
                         val tags : Set<CommonTag> = mutableSetOf(),
                         var timestamp : Date? = null)

data class CommonTag(var name : String? = null, var line : Int? = null)