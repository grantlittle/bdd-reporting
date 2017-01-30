package org.bdd.reporting.teamcity.plugin

import jetbrains.buildServer.serverSide.PropertiesProcessor
import jetbrains.buildServer.serverSide.RunType
import jetbrains.buildServer.serverSide.RunTypeRegistry
import jetbrains.buildServer.web.openapi.PluginDescriptor
import java.util.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
class BDDReportingRunType(runTypeRegistry: RunTypeRegistry, private val myPluginDescriptor: PluginDescriptor) : RunType() {

    init {
        runTypeRegistry.registerRunType(this)
    }

    override fun getType(): String {
        return BDDReportingConstants.RUNNER_TYPE
    }

    override fun getDisplayName(): String {
        return BDDReportingConstants.RUNNER_NAME
    }

    override fun getDescription(): String {
        return BDDReportingConstants.RUNNER_DESCRIPTION
    }

    override fun getRunnerPropertiesProcessor(): PropertiesProcessor? {
        return BDDReportingPropertiesProcessor()
    }

    override fun getEditRunnerParamsJspFilePath(): String? {
        return myPluginDescriptor.getPluginResourcesPath("editBDDReportingRunParams.jsp")
    }

    override fun getViewRunnerParamsJspFilePath(): String? {
        return myPluginDescriptor.getPluginResourcesPath("viewBDDReportingRunParams.jsp")
    }

    override fun getDefaultRunnerProperties(): Map<String, String>? {
        val defaultProperties = HashMap<String, String>()
        defaultProperties.put(BDDReportingConstants.BDD_REPORTING_BASE_DIR, "")
        defaultProperties.put(BDDReportingConstants.BDD_REPORTING_FILE_PATTERN, "cucumber/**/*.json")
        defaultProperties.put(BDDReportingConstants.BDD_REPORTING_URL, "http://localhost:8080")
        return defaultProperties
    }
}
