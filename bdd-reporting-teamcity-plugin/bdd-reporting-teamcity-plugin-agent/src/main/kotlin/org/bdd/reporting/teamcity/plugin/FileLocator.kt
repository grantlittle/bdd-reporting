package org.bdd.reporting.teamcity.plugin

import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.io.File

/**
 * Created by Grant Little grant@grantlittle.me
 */
fun findFilenames(basePath : String, pattern: String) : List<File> {
    val matcher = PathMatchingResourcePatternResolver()
    return matcher.getResources("file://$basePath/$pattern").map { it.file }
}

