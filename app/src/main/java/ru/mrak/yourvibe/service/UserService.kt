package ru.mrak.yourvibe.service

import com.google.firebase.auth.AuthResult
import ru.mrak.yourvibe.model.User
import ru.mrak.yourvibe.model.UserName
import ru.mrak.yourvibe.repository.AUTH
import ru.mrak.yourvibe.repository.DATABASE
import ru.mrak.yourvibe.repository.NODE_USERS
import ru.mrak.yourvibe.repository.NODE_USER_NAME
import ru.mrak.yourvibe.util.AppValueEventListener
import ru.mrak.yourvibe.util.firebase.getMap
import ru.mrak.yourvibe.util.showExceptionToast
import java.lang.RuntimeException
import kotlin.random.Random

object UserService {
    fun getUser(id: String, function: (User?) -> Unit) {
        DATABASE.child(NODE_USERS).child(id)
            .addListenerForSingleValueEvent(AppValueEventListener {
                function(it.getValue(User::class.java))
            })
    }

    fun signUp(function: (User) -> Unit) {
        signUpAnonymously { authResult ->
            createUser(authResult.user!!.uid) {
                function(it)
            }
        }
    }

    fun createUser(id: String, function: (User) -> Unit) {
        getUserName { userName ->
            val name = createName(userName)
            val user = User(id, name)
            createUser(user) {
                function(user)
            }
        }
    }

    private fun signUpAnonymously(function: (authResult: AuthResult) -> Unit) {
        AUTH.signInAnonymously()
            .addOnSuccessListener { function(it) }
            .addOnFailureListener { showExceptionToast(it) }
    }

    private fun createName(userName: UserName): String {
        val animal = userName.animals[Random.nextInt(userName.animals.size)]
            .split(",")[0]
        val type = userName.animals[Random.nextInt(userName.animals.size)]
            .split(",")[1].toInt()
        val personality = userName.personality[Random.nextInt(userName.personality.size)]
            .split(",")[type]
        return "$personality $animal"
    }

    private fun getUserName(function: (UserName) -> Unit) {
        DATABASE.child(NODE_USER_NAME)
            .addListenerForSingleValueEvent(AppValueEventListener {
                function(it.getValue(UserName::class.java) ?: throw RuntimeException())
            })
    }

    fun createUser(user: User, function: () -> Unit) {
        DATABASE.child(NODE_USERS).child(user.uid).setValue(getMap(user))
            .addOnSuccessListener { function() }
            .addOnFailureListener { showExceptionToast(it) }
    }
}