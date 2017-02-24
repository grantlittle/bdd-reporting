package org.bdd.reporting.events

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

/**
 */
@Service("DbEventBus")
@Suppress("UNCHECKED_CAST")
class EventBusImpl(val objectMapper : ObjectMapper) : EventBus {

    private val registrations : MutableMap<String, MutableList<(Any) -> Unit>> = mutableMapOf()


    override fun <T> send(name: String, event: T) {
//        val localEvent = DbEvent(name, UUID.randomUUID().toString(), Date(), objectMapper.writeValueAsString(event))
        sendEvents(name, event)

    }

    override fun <T> send(name: String, id : String, event: T) {
        sendEvents(name, event)
    }


    override fun <T> register(name: String, consumer : (T) -> Unit) {
        var list = registrations[name]
        if (null == list) {
            list = mutableListOf()
            registrations[name] = list
        }
        list.add(consumer as (Any) -> Unit)
    }

    private fun <T> sendEvents(topic : String, event : T) {
        registrations[topic]?.forEach {

            val handler = it as ((T) -> Unit)
            handler(event)
        }
    }
}