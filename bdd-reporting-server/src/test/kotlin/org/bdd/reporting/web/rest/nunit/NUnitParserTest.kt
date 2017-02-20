package org.bdd.reporting.web.rest.nunit

import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("UNCHECKED_CAST")
/**
 * Created by Grant Little grant@grantlittle.me
 */
class NUnitParserTest {


    @Test
    fun parse() {
        val resource = this.javaClass.classLoader.getResource("nunit-reporting.xml").openStream()

        val features = NUnitParser().parse(resource)
        assertEquals(1, features.size)
        val feature = features[0]
        assertEquals("Feature1", feature.name)
        assertEquals("SpecflowTest1.Features.Feature1Feature", feature.id)
        assertEquals(1, feature.tags.size)
        assertEquals("featuretag1", feature.tags.iterator().next().name)
        assertEquals(3, feature.scenarios.size)
        val scenario1 = feature.scenarios[0]

        assertEquals("Add three numbers", scenario1.name)
        assertEquals("SpecflowTest1.Features.Feature1Feature.AddThreeNumbers(\"50\",\"10\",\"10\",\"70\",System.String[])", scenario1.id)
        assertEquals(5, scenario1.steps.size)

        var step1 = scenario1.steps[0]
        assertEquals("I have entered 50 into the calculator", step1.name)
        assertEquals("Given", step1.keyword)
        assertEquals("passed", step1.result)

        var step2 = scenario1.steps[1]
        assertEquals("I have entered 10 into the calculator", step2.name)
        assertEquals("And", step2.keyword)
        assertEquals("passed", step2.result)

        var step4 = scenario1.steps[3]
        assertEquals("I press add", step4.name)
        assertEquals("When", step4.keyword)
        assertEquals("passed", step4.result)

        var step5 = scenario1.steps[4]
        assertEquals("the result should be 70 on the screen", step5.name)
        assertEquals("Then", step5.keyword)
        assertEquals("pending", step5.result)


    }


}