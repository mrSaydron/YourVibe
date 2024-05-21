package ru.mrak.yourvibe.ui.adapter

import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.enums.ListItemEnum
import ru.mrak.yourvibe.ui.holder.HolderVibe
import ru.mrak.yourvibe.util.VibeComparator
import java.util.concurrent.atomic.AtomicInteger

class ListEmptyAdapter(
    longClickListener: ((ListItemWrapper) -> Unit)? = null,
    comparator: Comparator<in ListItemWrapper>
) : ListAdapter(longClickListener, comparator) {

    private var itemSize: AtomicInteger = AtomicInteger(0)

    private var itemEmptyView = false

    private val itemEmpty = ListItemWrapper(ListItemEnum.ITEM_EMPTY, Any())

    init {
        super.updateListItems(itemEmpty)
        itemEmptyView = true
    }

    override fun updateListItems(item: ListItemWrapper) {
        super.updateListItems(item)
        itemSize.addAndGet(1)
        checkItemEmpty()
    }

    override fun removeListItem(holder: HolderVibe) {
        itemSize.addAndGet(-1)
        super.removeListItem(holder)
        checkItemEmpty()
    }

    private fun checkItemEmpty() {
        if (itemSize.get() == 0) {
            if (!itemEmptyView) {
                addItemEmpty()
                itemEmptyView = true
            }
        } else {
            if (itemEmptyView) {
                removeItemEmpty()
                itemEmptyView = false
            }
        }
    }

    private fun addItemEmpty() {
        super.updateListItems(itemEmpty)
    }

    private fun removeItemEmpty() {
        super.removeListItem(itemList.indexOf(itemEmpty))
    }
}