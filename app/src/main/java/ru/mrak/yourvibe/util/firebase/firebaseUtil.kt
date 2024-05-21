package ru.mrak.yourvibe.util.firebase

fun getNode(obj: Any): String {
    val clazz = obj::class.java
    val node = clazz.getAnnotation(Node::class.java)
    if (node == null) {
        throw RuntimeException()
    } else {
        return node.value
    }
}

fun getNode(clazz: Class<out Any>): String {
    val node = clazz.getAnnotation(Node::class.java)
    if (node == null) {
        throw RuntimeException()
    } else {
        return node.value
    }
}