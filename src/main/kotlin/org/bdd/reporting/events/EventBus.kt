package org.bdd.reporting.events


/**
 */
interface EventBus {

    fun <T> send(name : String, event : T)
    fun <T> send(name : String, id : String, event : T)
    fun <T> register(name : String, consumer : (T) -> Unit )
}

