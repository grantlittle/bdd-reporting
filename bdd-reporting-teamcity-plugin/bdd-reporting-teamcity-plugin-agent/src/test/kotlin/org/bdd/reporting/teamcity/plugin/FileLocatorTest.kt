package org.bdd.reporting.teamcity.plugin

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

/**
 * Created by Grant Little grant@grantlittle.me
 */
class FileLocatorTest {


    @Test
    fun findFiles() {
        val file = File(".").canonicalFile
        val files = findFilenames("$file/", "src/test/resources/**/*.txt")
        assertEquals(1, files.size)
    }

}