package ru.mrak.yourvibe.ui

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.repository.USER
import ru.mrak.yourvibe.ui.listVoted.ListVibeVotedFragment
import ru.mrak.yourvibe.ui.listOwner.ListOwnerFragment
import ru.mrak.yourvibe.ui.settings.SettingsFragment
import ru.mrak.yourvibe.ui.listVibe.ListVibeFragment
import ru.mrak.yourvibe.ui.listHistory.ListVibeHistoryFragment
import ru.mrak.yourvibe.ui.statistic.StatisticFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.DRAWER_VOTED_VIBES
import ru.mrak.yourvibe.util.DRAWER_OWN_VIBES
import ru.mrak.yourvibe.util.DRAWER_SETTINGS
import ru.mrak.yourvibe.util.DRAWER_STATISTIC
import ru.mrak.yourvibe.util.DRAWER_VIBES
import ru.mrak.yourvibe.util.DRAWER_VIBES_HISTORY
import ru.mrak.yourvibe.util.replaceFragment

class AppDrawer {
    private lateinit var drawer: Drawer
    private lateinit var header: AccountHeader
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var currentProfile: ProfileDrawerItem

    fun create() {
        initLoader()
        createHeader()
        createDrawer()
        drawerLayout = drawer.drawerLayout
    }

    private fun initLoader() {

    }

    private fun createHeader() {
        currentProfile = ProfileDrawerItem()
            .withName(USER.username)
            .withIdentifier(200)

        header = AccountHeaderBuilder()
            .withActivity(APP_ACTIVITY)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(currentProfile)
            .withProfileImagesVisible(false)
            .build()
    }

    private fun createDrawer() {
        drawer = DrawerBuilder()
            .withActivity(APP_ACTIVITY)
            .withToolbar(APP_ACTIVITY.toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(header)
            .addDrawerItems(
                PrimaryDrawerItem()
                    .withIdentifier(DRAWER_VIBES)
                    .withName("Вайбы")
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(DRAWER_OWN_VIBES)
                    .withName("Мои вайбы")
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(DRAWER_VOTED_VIBES)
                    .withName("Проголосованные вайбы")
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(DRAWER_VIBES_HISTORY)
                    .withName("История")
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(DRAWER_STATISTIC)
                    .withName("Статистика")
                    .withSelectable(false),
                PrimaryDrawerItem()
                    .withIdentifier(DRAWER_SETTINGS)
                    .withName("Настройки")
                    .withSelectable(false)
                    .withIconTintingEnabled(true)
                    .withIcon(R.drawable.ic_settings),
            ).withOnDrawerItemClickListener(object: Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (drawerItem.identifier) {
                        DRAWER_SETTINGS -> replaceFragment(SettingsFragment())
                        DRAWER_VIBES -> replaceFragment(ListVibeFragment())
                        DRAWER_OWN_VIBES -> replaceFragment(ListOwnerFragment())
                        DRAWER_VOTED_VIBES -> replaceFragment(ListVibeVotedFragment())
                        DRAWER_VIBES_HISTORY -> replaceFragment(ListVibeHistoryFragment())
                        DRAWER_STATISTIC -> replaceFragment(StatisticFragment())
                    }
                    return false
                }
            }).build()
    }

    fun updateProfile() {
        currentProfile
            .withName(USER.username)

        header.updateProfile(currentProfile)
    }

    fun enabledDrawer() {
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled( false ) // todo
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true // todo
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED) // todo
        APP_ACTIVITY.toolbar.setNavigationOnClickListener {
            drawer.openDrawer()
        }
    }

    fun disabledDrawer() {
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false // todo
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled( true ) // todo
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED) // todo
        APP_ACTIVITY.toolbar.setNavigationOnClickListener {
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
    }
}