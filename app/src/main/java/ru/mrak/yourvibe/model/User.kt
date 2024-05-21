package ru.mrak.yourvibe.model

import ru.mrak.yourvibe.repository.NODE_USERS
import ru.mrak.yourvibe.util.firebase.Node

@Node(NODE_USERS)
data class User(
    var uid: String = "",
    var username: String = "",
)
