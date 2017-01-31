package org.bdd.reporting

import org.springframework.context.annotation.Import

/**
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Import(BddReportingConfiguration::class)
annotation class EnableBddReporting