package ru.mrak.yourvibe.util

object EventSingleScope {
    private val listeners: MutableMap<Class<*>, MutableList<EventSingleValue<*>>> = mutableMapOf()

    fun add(clazz: Class<*>, listener: EventSingleValue<*>) {
        listeners.computeIfAbsent(clazz) { mutableListOf() }
        listeners[clazz]!!.add(listener)
    }

    fun clear(clazz: Class<*>) {
        listeners[clazz]?.forEach { listener ->
            listener.removeListener()
        }
        listeners[clazz]?.clear()
    }
}