package org.bdd.reporting.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration("ZooKeeperSettings")
@EnableConfigurationProperties(ZooKeeperSettings::class)
@ConfigurationProperties(prefix = "spring.cloud.zookeeper")
open class ZooKeeperSettings(var connectString : String = "localhost:2181")