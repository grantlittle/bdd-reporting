package org.bdd.reporting.teamcity.plugin

/**
 * Created by Grant Little grant@grantlittle.me
 */
class BDDReportingConfig(config: Map<String, String>) {

    var baseDirectory: String? = null
    var pattern: String? = null
    var url: String? = null
    var type: String? = null

    init {
        baseDirectory = config[BDDReportingConstants.BDD_REPORTING_BASE_DIR]
        pattern = config[BDDReportingConstants.BDD_REPORTING_FILE_PATTERN]
        url = config[BDDReportingConstants.BDD_REPORTING_URL]
        type = config[BDDReportingConstants.BDD_REPORTING_TYPE]
    }
}
