package ru.mrak.yourvibe.util.firebase

import ru.mrak.yourvibe.util.toSnake
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

val inOriginTypes = listOf(
    "kotlin.String",
    "kotlin.String?",
    "kotlin.Float",
    "kotlin.Float?",
    "kotlin.Int",
    "kotlin.Int?",
    "kotlin.collections.List<kotlin.String>",
    "kotlin.Any",
    "kotlin.Any?",
    "kotlin.Boolean",
    "kotlin.Boolean?",
)

fun getMap(obj: Any): Map<String, Any> {
    val result = hashMapOf<String, Any>()

    obj::class.memberProperties.forEach { property ->
        val key = getKey(property)
        if (property.getter.call(obj) != null) {
            if (inOriginTypes.contains(property.returnType.toString())) {
                result[key] = property.getter.call(obj) as Any
            } else {
                result[key] = getMap(property.getter.call(obj) as Any)
            }
        }
    }

    return result
}

fun getAnnotation(property: KProperty1<out Any, *>): MapName? {
    return try {
        property.annotations.filterIsInstance<MapName>().first()
    } catch (e: NoSuchElementException) {
        null
    }
}

fun getKey(property: KProperty1<out Any, *>): String {
    val mapNameAnnotation = getAnnotation(property)
    return if (mapNameAnnotation != null && mapNameAnnotation.value.isNotEmpty()) {
        mapNameAnnotation.value
    } else {
        property.name
    }
}
