package org.bdd.reporting.teamcity.plugin

import jetbrains.buildServer.serverSide.InvalidProperty
import jetbrains.buildServer.serverSide.PropertiesProcessor
import jetbrains.buildServer.util.StringUtil
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
class BDDReportingPropertiesProcessor : PropertiesProcessor {

    override fun process(properties: Map<String, String>): Collection<InvalidProperty> {
        val result = Vector<InvalidProperty>()

        handleEmptyProperty(result,
                BDDReportingConstants.BDD_REPORTING_BASE_DIR,
                properties[BDDReportingConstants.BDD_REPORTING_BASE_DIR],
                "Reporting base folder must not be null")
        //        handleEmptyProperty(result,
        //                DockerConstants.DOCKER_USERNAME,
        //                properties.get(DockerConstants.DOCKER_USERNAME),
        //                "Username must be a non empty string");
        //        handleEmptyProperty(result,
        //                DockerConstants.DOCKER_PASSWORD,
        //                properties.get(DockerConstants.DOCKER_PASSWORD),
        //                "Password must be a non empty string");
        //        handleEmptyProperty(result,
        //                DockerConstants.DOCKER_EMAIL,
        //                properties.get(DockerConstants.DOCKER_EMAIL),
        //                "Email must be a non empty string");
        handleEmptyProperty(result,
                BDDReportingConstants.BDD_REPORTING_FILE_PATTERN,
                properties[BDDReportingConstants.BDD_REPORTING_FILE_PATTERN],
                "File search pattern should not be null")
        handleEmptyProperty(result,
                BDDReportingConstants.BDD_REPORTING_URL,
                properties[BDDReportingConstants.BDD_REPORTING_URL],
                "BDD Reporting server should not be null")
        handleEmptyProperty(result,
                BDDReportingConstants.BDD_REPORTING_TYPE,
                properties[BDDReportingConstants.BDD_REPORTING_TYPE],
                "File type should not be null")


        return result
    }

    private fun handleEmptyProperty(invalidFields: MutableList<InvalidProperty>, name: String, value: String?, message: String) {
        if (null == value || StringUtil.isEmpty(value)) {
            invalidFields.add(InvalidProperty(name, message))
        }
    }
}