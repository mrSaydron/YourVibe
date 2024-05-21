package ru.mrak.yourvibe.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import ru.mrak.yourvibe.model.User

lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID: String
lateinit var DATABASE: DatabaseReference
lateinit var USER: User

const val NODE_USER_NAME = "user_name"
const val NODE_USERS = "users"
const val NODE_VIBES = "vibes"
const val NODE_OWNER = "vibeOwner"
const val NODE_VOTED = "vibeVoted"

// NODE_VIBES
const val VALUE_AGREE_COUNT = "agreeCount"
const val VALUE_DISAGREE_COUNT = "disagreeCount"
