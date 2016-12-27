package org.bdd.reporting

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.context.annotation.Configuration

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
@ConditionalOnProperty(name=arrayOf("enabled"), prefix = "sso", havingValue = "true", matchIfMissing = true)
//@Profile("!noauth")
@EnableOAuth2Sso
open class SsoConfiguration