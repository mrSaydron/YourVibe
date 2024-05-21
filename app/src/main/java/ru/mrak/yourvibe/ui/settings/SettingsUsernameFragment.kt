package ru.mrak.yourvibe.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mrak.yourvibe.databinding.FragmentSettingsUsernameBinding
import ru.mrak.yourvibe.repository.USER
import ru.mrak.yourvibe.service.UserService
import ru.mrak.yourvibe.ui.group.VibeGroupsFragment
import ru.mrak.yourvibe.ui.base.BaseChangeFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.replaceFragment
import ru.mrak.yourvibe.util.showToast

class SettingsUsernameFragment : BaseChangeFragment() {

    private lateinit var binding: FragmentSettingsUsernameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsUsernameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initFields()
    }

    private fun initFields() {
        APP_ACTIVITY.title = "Имя пользователя"
        binding.settingsUsernameInput.setText(USER.username)
    }

    override fun change() {
        val newUsername = binding.settingsUsernameInput.text.toString()
        if (newUsername.isEmpty()) {
            showToast("Поле пустое")
        } else {
            USER.username = newUsername
            UserService.createUser(USER) {
                APP_ACTIVITY.appDrawer.updateProfile()
                replaceFragment(VibeGroupsFragment(), false)
            }
        }
    }
}