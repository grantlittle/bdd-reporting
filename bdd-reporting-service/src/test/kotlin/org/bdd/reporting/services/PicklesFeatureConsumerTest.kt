package org.bdd.reporting.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.io.IOUtils
import org.bdd.reporting.events.EventBus
import org.bdd.reporting.web.rest.pickles.PickleRoot
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Created by Grant Little grant@grantlittle.me
 */
class PicklesFeatureConsumerTest {

    @Test
    fun parse() {
        val mapper = ObjectMapper()
        val json = IOUtils.toString(this.javaClass.classLoader.getResource("pickles-output.json").openStream())
        val root = mapper.readValue(json, PickleRoot::class.java)

        val eventBus = mock(EventBus::class.java)

        val consumer = PicklesFeatureConsumer(eventBus)
        val result = consumer.mapPickleFeatures(root.features!!, emptySet())

        assertNotNull(result)


    }

}