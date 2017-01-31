package org.bdd.reporting

import org.bdd.reporting.data.DbEvent
import org.bdd.reporting.repository.elasticsearch.FeatureRepository
import org.bdd.reporting.repository.jpa.EventRepository
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 */
@Suppress("SpringFacetCode", "unused")
@ComponentScan(basePackageClasses = arrayOf(BddReportingConfiguration::class, EventRepository::class))
@Configuration
@EnableJpaRepositories(basePackageClasses = arrayOf(EventRepository::class))
@EnableElasticsearchRepositories(basePackageClasses = arrayOf(FeatureRepository::class))
@EntityScan(basePackageClasses = arrayOf(DbEvent::class))
open class BddReportingConfiguration {

    @Configuration
    @ConfigurationProperties(prefix = "")
    @EnableConfigurationProperties(BddReportingProperties::class)
    open class BddReportingProperties {

    }
}