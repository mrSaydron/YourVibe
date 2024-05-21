package ru.mrak.yourvibe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.mrak.yourvibe.databinding.ActivityMainBinding
import ru.mrak.yourvibe.repository.AUTH
import ru.mrak.yourvibe.repository.CURRENT_UID
import ru.mrak.yourvibe.repository.Database
import ru.mrak.yourvibe.repository.USER
import ru.mrak.yourvibe.service.UserService
import ru.mrak.yourvibe.ui.AppDrawer
import ru.mrak.yourvibe.ui.listVibe.ListVibeFragment
import ru.mrak.yourvibe.ui.vibe.VibeFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.replaceFragment

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    lateinit var toolbar: Toolbar
    lateinit var appDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        APP_ACTIVITY = this

        Database.initFirebase {
            auth {
                initFields()
                initFunc()
            }
        }
    }

    private fun initFields() {
        toolbar = binding.mainToolbar
        appDrawer = AppDrawer()
    }

    private fun initFunc() {
        setSupportActionBar(toolbar)
        appDrawer.create()
        replaceFragment(ListVibeFragment())
    }

    private fun auth(function: () -> Unit) {
        val currentUser = AUTH.currentUser
        if (currentUser == null) {
            UserService.signUp {
                CURRENT_UID = it.uid
                USER = it
                function()
            }
        } else {
            UserService.getUser(currentUser.uid) { user ->
                CURRENT_UID = currentUser.uid
                if (user != null) {
                    USER = user
                } else {
                    UserService.createUser(CURRENT_UID) {
                        USER = it
                        function()
                    }
                }
                function()
            }
        }
    }
}