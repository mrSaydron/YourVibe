package ru.mrak.yourvibe.ui.base

import androidx.fragment.app.Fragment
import ru.mrak.yourvibe.util.APP_ACTIVITY

open class BurgerFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.appDrawer.enabledDrawer()
    }
}