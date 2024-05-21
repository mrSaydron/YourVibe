package ru.mrak.yourvibe.util.firebase

@Retention( AnnotationRetention.RUNTIME )
@Target( AnnotationTarget.CLASS )
annotation class Node(
    val value: String
)
