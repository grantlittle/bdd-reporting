package org.bdd.reporting

import org.springframework.context.annotation.Import

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Target(AnnotationTarget.CLASS)
@Import(SsoConfiguration::class)
annotation class EnableSso