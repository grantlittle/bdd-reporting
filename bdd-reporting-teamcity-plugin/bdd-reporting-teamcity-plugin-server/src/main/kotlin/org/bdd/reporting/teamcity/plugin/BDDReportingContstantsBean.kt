package org.bdd.reporting.teamcity.plugin

/**
 * Created by Grant Little grant@grantlittle.me
 */
class BDDReportingContstantsBean {

    val baseDir: String
        get() = BDDReportingConstants.BDD_REPORTING_BASE_DIR

    val pattern: String
        get() = BDDReportingConstants.BDD_REPORTING_FILE_PATTERN

    val url: String
        get() = BDDReportingConstants.BDD_REPORTING_URL

    val type: String
        get() = BDDReportingConstants.BDD_REPORTING_TYPE


}
