package org.bdd.reporting.web.rest.pickles

/**
 * Created by Grant Little grant@grantlittle.me
 */
object Utils {

    @JvmStatic
    fun featureId(info: PickleFeatureInfo): String {
        return info.relativeFolder!!.toLowerCase().replace("\\", "_").replace(" ", "_")
    }

    @JvmStatic
    fun scenarioId(`in`: PickleScenario, featureId: String): String {
        return featureId + "::" + `in`.name!!.replace("\\", "_").replace(" ", "_")
    }

    @JvmStatic
    fun scenarioId(`in`: PickleScenario, featureId: String, index: Int): String {
        return "$featureId::${`in`.name!!.replace("\\", "_").replace(" ", "_")}::$index"
    }

}
