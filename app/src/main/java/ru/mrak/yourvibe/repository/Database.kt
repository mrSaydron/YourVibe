package ru.mrak.yourvibe.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import ru.mrak.yourvibe.model.User
import ru.mrak.yourvibe.model.Vibe
import ru.mrak.yourvibe.util.AppValueChildListener
import ru.mrak.yourvibe.util.AppValueEventListener
import ru.mrak.yourvibe.util.firebase.getMap
import ru.mrak.yourvibe.util.firebase.getNode
import ru.mrak.yourvibe.util.showExceptionToast

object Database {
    fun initFirebase(function: () -> Unit) {
        AUTH = FirebaseAuth.getInstance()
        CURRENT_UID = AUTH.currentUser?.uid.toString()
        DATABASE = FirebaseDatabase.getInstance().reference
        USER = User()
        function()
    }

    fun getKey(): String {
        return DATABASE.push().key.toString()
    }

    fun getCurrentTime(): Any {
        return ServerValue.TIMESTAMP
    }

    fun save(id: String, entity: Any, function: () -> Unit) {
        DATABASE.child(getNode(entity)).child(id).setValue(getMap(entity))
            .addOnSuccessListener { function() }
            .addOnFailureListener { showExceptionToast(it) }
    }

    fun saveOneValue(node: String, key: String, value: Any, function: () -> Unit) {
        DATABASE.child(node).child(key).setValue(value)
            .addOnSuccessListener { function() }
            .addOnFailureListener { showExceptionToast(it) }
    }

    fun saveOneValue(node: String, value: Any, function: () -> Unit) {
        DATABASE.child(node).setValue(value)
            .addOnSuccessListener { function() }
            .addOnFailureListener { showExceptionToast(it) }
    }

    fun incrementOneValue(node: String, value: Long, function: () -> Unit) {
        DATABASE.child(node).setValue(ServerValue.increment(value))
            .addOnSuccessListener { function() }
            .addOnFailureListener { showExceptionToast(it) }
    }

    fun getValueListener(clazz: Class<out Any>, function: (Any) -> Unit) {
        DATABASE.child(getNode(clazz)).addChildEventListener(
            AppValueChildListener { dataSnapshot ->
                val result = dataSnapshot.getValue(clazz)
                result?.let { function(result) }
        })
    }

    fun <T> getValueListener(node: String, clazz: Class<T>, function: (T) -> Unit) {
        DATABASE.child(node).addChildEventListener(
            AppValueChildListener { dataSnapshot ->
                val result = dataSnapshot.getValue(clazz)
                dataSnapshot.key
                result?.let(function)
        })
    }

    fun <T> getValuePairListener(node: String, clazz: Class<T>, function: (Pair<String, T>) -> Unit) {
        DATABASE.child(node).addChildEventListener(
            AppValueChildListener { dataSnapshot ->
                val result = dataSnapshot.getValue(clazz)
                if (dataSnapshot.key != null && result != null) {
                    function(Pair(dataSnapshot.key!!, result))
                }
        })
    }

    fun <T> getSingleListener(node: String, clazz: Class<T>, function: (T?) -> Unit) {
        DATABASE.child(node).addListenerForSingleValueEvent(
            AppValueEventListener { dataSnapshot ->
                val result = dataSnapshot.getValue(clazz)
                function(result)
            })
    }

    fun getSingleListener(node: String, function: (HashMap<*, *>) -> Unit) {
        DATABASE.child(node).addListenerForSingleValueEvent(
            AppValueEventListener { dataSnapshot ->
                function(dataSnapshot.getValue() as HashMap<*, *>)
            })
    }

    fun <T> getSinglePairListener(node: String, clazz: Class<T>, function: (Pair<String, T>) -> Unit) {
        DATABASE.child(node).addListenerForSingleValueEvent(
            AppValueEventListener { dataSnapshot ->
                val result = dataSnapshot.getValue(clazz)
                if (dataSnapshot.key != null && result != null) {
                    function(Pair(dataSnapshot.key!!, result))
                }
            })
    }
}