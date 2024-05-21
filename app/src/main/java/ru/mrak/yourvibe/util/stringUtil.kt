package ru.mrak.yourvibe.util

fun toSnake(str: String): String {
    val stringBuilder = StringBuilder()
    str.forEach { char ->
        if (char <= 'Z') {
            stringBuilder.append('_')
            stringBuilder.append(char + ('a' - 'A'))
        } else {
            stringBuilder.append(char)
        }
    }
    return stringBuilder.toString()
}