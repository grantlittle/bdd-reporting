package org.bdd.reporting.web.rest.nunit

import org.bdd.reporting.data.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.BufferedReader
import java.io.InputStream
import java.io.StringReader
import java.text.SimpleDateFormat
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory


@Suppress("UNCHECKED_CAST")
/**
 * Created by Grant Little grant@grantlittle.me
 */
class NUnitParser {

    companion object {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ssX")
    }

    fun parse(inputStream : InputStream, properties: Set<CommonProperty>) : List<CommonFeature> {
        val dbf = DocumentBuilderFactory.newInstance()
        dbf.isNamespaceAware = false
        dbf.isValidating = false
        val db = dbf.newDocumentBuilder()
        val document = db.parse(inputStream)
        val xPath = XPathFactory.newInstance().newXPath()
        return parseFeatures(document, xPath, properties)

    }


    private fun parseFeatures(result: Document, xPath: XPath, properties: Set<CommonProperty>): MutableList<CommonFeature> {
        val nodeList = xPath.evaluate("//test-suite[@type = 'TestFixture']", result.documentElement, XPathConstants.NODESET) as NodeList
        val features = mutableListOf<CommonFeature>()

        // Loop around features
        for (i in 0..nodeList.length - 1) {
            val element = nodeList.item(i) as Element
            features.add(parseFeature(element, properties))

        }
        return features
    }


    internal fun parseFeature(element: Element, properties: Set<CommonProperty>) : CommonFeature {
        val featureFullName = element.getAttribute("fullname")
        val featureProperties = readProperties(element)
        val timestampElement = element.getAttribute("end-time")
        val timestamp = sdf.parse(timestampElement)

        val children = element.childNodes
        val scenarios = mutableListOf<CommonScenario>()
        if (children.length > 0) {
            for (childIndex in 0..children.length - 1) {
                val childElement = children.item(childIndex)
                when (childElement.nodeName) {
                    "test-case" -> {
                        scenarios.add(parseScenario(childElement))
                    }
                    "test-suite" -> {
                        scenarios.addAll(parseScenarioOverview(childElement as Element))
                    }
                }
            }
        }
        return CommonFeature(id = featureFullName,
                tags = featureProperties["tags"] as MutableSet<CommonTag>,
                scenarios = scenarios,
                name = featureProperties["name"] as String,
                timestamp = timestamp,
                properties = properties)
    }

    internal fun parseScenario(childElement: Node?) : CommonScenario {
        val testCaseElement = childElement as Element
        val fullNameAttribute = testCaseElement.getAttribute("fullname")

        val steps = parseStepsParent(testCaseElement)
        val scenarioProperties = readProperties(testCaseElement)
        return CommonScenario(id = fullNameAttribute,
                name = scenarioProperties["name"] as String,
                keyword = "Scenario",
                type = "Scenario",
                tags = scenarioProperties["tags"] as MutableSet<CommonTag>,
                steps = steps)
    }

    internal fun parseScenarioOverview(element : Element) : List<CommonScenario> {
        val overviewProperties = readProperties(element)
        val name = overviewProperties["name"] as String
        val tags = overviewProperties["tags"] as MutableSet<CommonTag>
        val scenarios = mutableListOf<CommonScenario>()

        val children = element.childNodes
        if (children.length > 0) {
            for (exampleIndex in 0..children.length-1) {
                val node = children.item(exampleIndex)
                when (node.nodeName) {
                    "test-case" -> {
                        val exampleElement = node as Element
                        val fullNameAttribute = exampleElement.getAttribute("fullname")

                        val steps = parseStepsParent(exampleElement)
                        scenarios.add(CommonScenario(id = fullNameAttribute,
                                name = name,
                                keyword = "Scenario",
                                type = "Scenario",
                                tags = tags,
                                steps = steps))
                    }
                }
            }
        }
        return scenarios

    }

    fun parseStepsParent(parent: Element) : List<CommonStep> {
        val children = parent.childNodes
        for (childIndex in 0..children.length-1) {
            val child = children.item(childIndex)
            when(child.nodeName) {
                "output" -> {
                    return parseStepsText(child.textContent)
                }
            }
        }
        return emptyList()
    }

    fun parseStepsText(text : String) : List<CommonStep> {
        val outcomeRegex = Regex("-> (.*?):.*\\((.*)\\).*")
        val keywordRegex = Regex("(.*?) (.*)")
        val reader = BufferedReader(StringReader(text))
        var line = reader.readLine()
        val steps = mutableListOf<CommonStep>()
        var description = ""
        var state : String? = null
        var keyword : String? = null
        while (line != null) {
            val result = outcomeRegex.matchEntire(line)
            if (result != null) {
                when (result.groupValues[1]) {
                    "done" -> {
                        state = "passed"
                    }
                    "failed" -> {
                        state = "failed"
                    }
                    "pending" -> {
                        state = "pending"
                    }
                }
                steps.add(CommonStep(name = description, keyword = keyword, result = state, line = null))
                description = ""
            } else {
                val keywordResult = keywordRegex.matchEntire(line)
                keyword = keywordResult!!.groupValues[1]
                description += keywordResult.groupValues[2]
            }
            line = reader.readLine()
        }
        return steps
    }

    fun readProperties(element : Element) : Map<String, Any> {
        var name = ""
        val tags = mutableSetOf<CommonTag>()
        val propertiesElements = element.getElementsByTagName("properties")
        if (null != propertiesElements && propertiesElements.length > 0) {
            val propertiesElement = propertiesElements.item(0) as Element
            val propertyElements = propertiesElement.getElementsByTagName("property")
            if (propertyElements != null && propertyElements.length > 0) {
                for (propertyIndex in 0..propertyElements.length-1) {
                    val propertyElement = propertyElements.item(propertyIndex) as Element
                    val value = propertyElement.getAttribute("value")
                    when (propertyElement.getAttribute("name")) {
                        "Description" -> name = value
                        "Category" -> tags.add(CommonTag(name = value))
                    }
                }
            }
        }
        return mapOf(Pair("name", name), Pair("tags", tags))
    }

}