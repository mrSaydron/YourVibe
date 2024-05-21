package ru.mrak.yourvibe.ui.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.mrak.yourvibe.model.ListItemWrapper

abstract class HolderAbstract(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun initFields(item: ListItemWrapper)
}