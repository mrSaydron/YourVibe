package ru.mrak.yourvibe.util

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.mrak.yourvibe.MainActivity
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.Vibe
import ru.mrak.yourvibe.model.enums.VibeOwnListOrder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun showExceptionToast(e: Exception) {
    Toast.makeText(APP_ACTIVITY, e.message.toString(), Toast.LENGTH_LONG).show()
}

fun replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    hideKeyboard()
    if (addStack) {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_data_container, fragment)
            .commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(R.id.main_data_container, fragment)
            .commit()
    }
}

fun hideKeyboard() {
    val imm: InputMethodManager = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun restartApp() {
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.startActivity(intent)
    APP_ACTIVITY.finish()
}

fun String.asDateString(): String {
    if (this.isNotEmpty()) {
        val date = Date(this.toLong())
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    } else {
        return this
    }
}

fun String.asDate(): Date {
    return Date(this.toLong())
}

fun MutableList<Vibe>.insert(vibe: Vibe, comparator: Comparator<in Vibe>): Int {
    this.sortWith(comparator)
    return this.indexOf(vibe)
}

fun MutableList<ListItemWrapper>.insert(item: ListItemWrapper, comparator: Comparator<in ListItemWrapper>): Int {
    this.sortWith(comparator)
    return this.indexOf(item)
}