package ru.mrak.yourvibe.model

data class UserName(
    var animals: List<String> = emptyList(),
    var animalsCount: Int = 0,
    var codeValue: Int = 0,
    var length: Int = 0,
    var personality: List<String> = emptyList(),
    var personalityCount: Int = 0,
    var step: Int = 0,
)
