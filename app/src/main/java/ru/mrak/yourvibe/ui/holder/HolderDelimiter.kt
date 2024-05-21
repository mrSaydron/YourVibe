package ru.mrak.yourvibe.ui.holder

import android.view.View
import ru.mrak.yourvibe.databinding.ItemDelimiterBinding
import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.util.EventSingleValue

class HolderDelimiter(view: View) : HolderAbstract(view) {

    private val binding = ItemDelimiterBinding.bind(view)

    override fun initFields(item: ListItemWrapper) {
        if (item.item is EventSingleValue<*>) {
            item.item.setListener { isVisible ->
                if (isVisible is Boolean && isVisible) {
                    binding.root.visibility = View.VISIBLE
                } else {
//                    binding.root.visibility = View.GONE
                    binding.root.visibility = View.VISIBLE
                }
            }
        }
    }
}