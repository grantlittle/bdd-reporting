package org.bdd.reporting.teamcity.plugin

import jetbrains.buildServer.agent.BuildProgressLogger
import org.apache.commons.io.FileUtils
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import java.io.File

/**
 * Created by Grant Little grant@grantlittle.me
 */
interface Processor {
    fun process(filenames: List<File>, buildProgressLogger: BuildProgressLogger, config : BDDReportingConfig)
}

open class DefaultProcessor(private val context : String) : Processor {
    override fun process(filenames: List<File>, buildProgressLogger: BuildProgressLogger, config : BDDReportingConfig) {
        for (filename in filenames) {
            buildProgressLogger.message("Loading resource $filename")
            val builder = HttpClientBuilder.create();
            val put = HttpPut("${config.url}$context");
            put.addHeader("Content-Type", "application/json")
            val entity = StringEntity(FileUtils.readFileToString(filename), ContentType.APPLICATION_JSON);
            put.entity = entity;
            val response = builder.build().execute(put);
            if (200 != response.statusLine.statusCode) {
                buildProgressLogger.buildFailureDescription("Failed to upload results: ${response.statusLine.reasonPhrase}")
            } else {
                buildProgressLogger.message("Resource uploaded: $filename")
            }
        }
    }

}

class CucumberProcessor() : DefaultProcessor("/api/1.0/features/cucumber")
class PicklesProcessor() : DefaultProcessor("/api/1.0/features/pickles")
