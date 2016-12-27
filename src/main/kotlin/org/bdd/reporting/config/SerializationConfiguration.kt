package org.bdd.reporting.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
open class SerializationConfiguration {

    @Bean
    @Primary
    open fun objectMapper(builder : Jackson2ObjectMapperBuilder) : ObjectMapper {
        val objectMapper = builder.build<ObjectMapper>()
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        return objectMapper
    }
}
