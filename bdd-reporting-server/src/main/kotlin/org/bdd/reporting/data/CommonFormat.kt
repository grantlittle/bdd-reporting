package org.bdd.reporting.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.elasticsearch.annotations.Document
import java.io.Serializable
import java.math.BigInteger
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "features")
data class CommonFeature(var id : String? = null,
                         var name : String? = null,
                         var description : String? = "",
                         val labels : Set<String>? = mutableSetOf(),
                         val tags : Set<CommonTag> = mutableSetOf(),
                         var timestamp : Date? = null,
                         var scenarios : List<CommonScenario> = mutableListOf())

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommonTag(var name : String? = null, var line : Int? = null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommonScenario(var id : String? = null,
                          var name : String? = null,
                          var description : String? = null,
                          var tags : Set<CommonTag> = mutableSetOf(),
                          var type: String? = null,
                          var keyword: String? = null,
                          var line : Int? = null,
                          var steps: List<CommonStep> = mutableListOf())

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommonStep(
        var line: Int? = 0,
        var name: String? = null,
        var keyword: String? = null,
        var result: String? = null)

@Entity
@IdClass(DbEventKey::class)
open class DbEvent(@Id var topic : String? = null,
                   @Id var id: String? = null,
                   @Id var timestamp : Date? = null,
                   @Lob
                 @Column(length = 100000)
                 var data : String? = null)

open class DbEventKey(var topic : String? = null,
                      var id: String? = null,
                      var timestamp : Date? = null) : Serializable

