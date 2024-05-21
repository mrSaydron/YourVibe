package ru.mrak.yourvibe.util

class EventListener {
    private val listeners = mutableMapOf<String, (key: String) -> Unit>()

    fun addListener(id: String, function: (key: String) -> Unit) {
        listeners[id] = function
    }

    fun removeListener(id: String) {
        listeners.remove(id)
    }

    fun event(key: String) {
        listeners.values.forEach { it(key) }
    }
}