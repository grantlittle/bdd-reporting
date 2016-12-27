package org.bdd.reporting

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
@Profile("!noauth")
@EnableOAuth2Sso
open class SsoConfiguration