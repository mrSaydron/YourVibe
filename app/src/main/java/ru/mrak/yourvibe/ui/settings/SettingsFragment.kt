package ru.mrak.yourvibe.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.databinding.FragmentSettingsBinding
import ru.mrak.yourvibe.repository.AUTH
import ru.mrak.yourvibe.repository.USER
import ru.mrak.yourvibe.ui.base.BackFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.replaceFragment
import ru.mrak.yourvibe.util.restartApp

class SettingsFragment : BackFragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        APP_ACTIVITY.title = "Настройки"

        binding.settingsUsername.text = USER.username
        binding.settingsEditUsername.text = USER.username

        binding.settingsBlockUsername.setOnClickListener {
            replaceFragment(SettingsUsernameFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                restartApp()
            }
        }
        return true
    }
}