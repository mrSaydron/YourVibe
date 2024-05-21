package ru.mrak.yourvibe.ui.base

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import ru.mrak.yourvibe.MainActivity
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.hideKeyboard

open class BaseChangeFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        APP_ACTIVITY.appDrawer.disabledDrawer()
        hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.menu_btn_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_btn_confirm -> change()
        }
        return true
    }

    open fun change() {

    }
}