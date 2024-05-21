package ru.mrak.yourvibe.util

class EventValue<T>(private var value: T) {
    private var listener: ((T) -> Unit)? = null

//    private var value: T = value

    fun set(value: T) {
        this.value = value
        listener?.let { it(value) }
    }

    fun get(): T {
        return value
    }

    fun setListener(listener: (T) -> Unit) {
        this.listener = listener
        value?.let { listener(it) }
    }

    fun removeListener() {
        listener = null
    }
}