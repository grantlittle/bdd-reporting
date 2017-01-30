package org.bdd.reporting.teamcity.plugin

import jetbrains.buildServer.RunBuildException
import jetbrains.buildServer.agent.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
class BDDReportingProcess internal constructor(runningBuild: AgentRunningBuild, private val context: BuildRunnerContext) : BuildProcessAdapter() {
    private val buildProgressLogger: BuildProgressLogger

    private val cucumberProcessor = CucumberProcessor()
    private val picklesProcessor = PicklesProcessor()

    init {
        this.buildProgressLogger = runningBuild.buildLogger
    }

    @Throws(RunBuildException::class)
    override fun start() {
        buildProgressLogger.message("BDD Reporting started")

        val mapConfig = context.runnerParameters

        val config = BDDReportingConfig(mapConfig)

        val filenames = findFilenames(basePath = context.workingDirectory.absolutePath as String, pattern = config.pattern as String)


        if (config.type == "cucumber") {
            cucumberProcessor.process(filenames, buildProgressLogger, config)
        } else if (config.type == "pickles") {
            picklesProcessor.process(filenames, buildProgressLogger, config)
        } else {
            throw IllegalStateException("Unsupported format ${config.type}")
        }

    }

    @Throws(RunBuildException::class)
    override fun waitFor(): BuildFinishedStatus {
        return BuildFinishedStatus.FINISHED_SUCCESS
    }

}
