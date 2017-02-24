package org.bdd.reporting.web.rest.cucumber

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

/**
 * Created by Grant Little grant@grantlittle.me
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class CucumberMatch : Serializable {

    var location: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val match = other as CucumberMatch?

        return if (location != null) location == match!!.location else match!!.location == null

    }

    override fun hashCode(): Int {
        return if (location != null) location!!.hashCode() else 0
    }

    override fun toString(): String {
        val sb = StringBuilder("Match{")
        sb.append("location='").append(location).append('\'')
        sb.append('}')
        return sb.toString()
    }

    companion object {
        private val serialVersionUID = -2837759739267229507L
    }
}
