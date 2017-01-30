package org.bdd.reporting.teamcity.plugin

/**
 * Created by Grant Little grant@grantlittle.me
 */
interface BDDReportingConstants {
    companion object {
        val RUNNER_TYPE = "BDDReportingRunner"
        val RUNNER_NAME = "BDD Reporting Runner"
        val RUNNER_DESCRIPTION = "A Runner that reports on BDD tests to the central reporting server"
        val BDD_REPORTING_BASE_DIR = "bdd.reporting.base.dir"
        val BDD_REPORTING_FILE_PATTERN = "bdd.reporting.file.pattern"
        val BDD_REPORTING_URL = "bdd.reporting.url"
        val BDD_REPORTING_TYPE = "bdd.reporting.type"
    }

}
