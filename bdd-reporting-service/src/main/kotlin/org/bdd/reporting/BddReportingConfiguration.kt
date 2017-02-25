package org.bdd.reporting

import org.bdd.reporting.repository.elasticsearch.FeatureRepository
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

/**
 */
@Suppress("SpringFacetCode", "unused")
@ComponentScan(basePackageClasses = arrayOf(BddReportingConfiguration::class))
@Configuration
@EnableElasticsearchRepositories(basePackageClasses = arrayOf(FeatureRepository::class))
open class BddReportingConfiguration {

    @Configuration
    @ConfigurationProperties(prefix = "bdd.reporting")
    @EnableConfigurationProperties(BddReportingProperties::class)
    open class BddReportingProperties
}