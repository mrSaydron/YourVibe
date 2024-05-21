package ru.mrak.yourvibe.ui.holder

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.mrak.yourvibe.databinding.ItemEmptyBinding
import ru.mrak.yourvibe.databinding.StatisticItemBinding
import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.service.StatisticService
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.EventSingleValue

class HolderEmpty(view: View) :  HolderAbstract(view) {

    private val binding = ItemEmptyBinding.bind(view)

    override fun initFields(item: ListItemWrapper) {
        if (item.item is EventSingleValue<*>) {
            item.item.setListener { isVisible ->
                if (isVisible is Boolean && isVisible) {
                    binding.root.visibility = View.VISIBLE
                } else {
                    binding.root.visibility = View.GONE
                }
            }
        }
    }
}