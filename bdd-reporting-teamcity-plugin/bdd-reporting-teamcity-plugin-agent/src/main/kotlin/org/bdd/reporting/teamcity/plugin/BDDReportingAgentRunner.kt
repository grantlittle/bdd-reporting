package org.bdd.reporting.teamcity.plugin

import jetbrains.buildServer.RunBuildException
import jetbrains.buildServer.agent.*

/**
 * Created by Grant Little grant@grantlittle.me
 */
class BDDReportingAgentRunner : BuildProcessAdapter(), AgentBuildRunner, AgentBuildRunnerInfo {


    @Throws(RunBuildException::class)
    override fun createBuildProcess(runningBuild: AgentRunningBuild, context: BuildRunnerContext): BuildProcess {
        return BDDReportingProcess(runningBuild, context)
    }

    override fun getRunnerInfo(): AgentBuildRunnerInfo {
        return this
    }

    override fun getType(): String {
        return BDDReportingConstants.RUNNER_TYPE
    }

    override fun canRun(agentConfiguration: BuildAgentConfiguration): Boolean {
        return true
    }
}
