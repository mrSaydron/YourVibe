package ru.mrak.yourvibe.util

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class EventSingleValue<T : Any>(private var value: T) {
    private var listener: ((T) -> Unit)? = null

    fun set(value: T) {
        println("set")
        this.value = value
        listener?.let { APP_ACTIVITY.lifecycleScope.launch { it(value) } }
    }

    fun get(): T {
        return value
    }

    fun setListener(clazz: Class<*>, listener: (T) -> Unit) {
        EventSingleScope.add(clazz, this)
        setListener(listener)
    }

    fun setListener(listener: (T) -> Unit) {
        println("set listener")
        if (this.listener != null) {
            throw RuntimeException("Listener not empty")
        }
        this.listener = listener
        value.let { APP_ACTIVITY.lifecycleScope.launch { listener(it) } }
    }

    fun removeListener() {
        listener = null
    }
}