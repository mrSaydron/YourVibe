package ru.mrak.yourvibe.ui.adapter

import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.enums.ListItemEnum
import ru.mrak.yourvibe.ui.holder.HolderVibe
import ru.mrak.yourvibe.util.VibeComparator
import java.util.concurrent.atomic.AtomicInteger

class ListOwnerAdapter(
    longClickListener: ((ListItemWrapper) -> Unit)? = null
) : ListAdapter(longClickListener, VibeComparator.vibeOwner) {

    private var itemExpiredSize: AtomicInteger = AtomicInteger(0)
    private var itemWaitingSize: AtomicInteger = AtomicInteger(0)
    private var itemFinishedSize: AtomicInteger = AtomicInteger(0)

    private var itemEmptyView = false
    private var delimiterFinishedView = false
    private var delimiterWaitingView = false

    private val itemEmpty = ListItemWrapper(ListItemEnum.ITEM_EMPTY, Any())
    private val delimiterFinished = ListItemWrapper(ListItemEnum.DELIMITER_FINISHED, Any())
    private val delimiterWaiting = ListItemWrapper(ListItemEnum.DELIMITER_WAITING, Any())

    init {
        super.updateListItems(itemEmpty)
        itemEmptyView = true
    }

    override fun updateListItems(item: ListItemWrapper) {
        super.updateListItems(item)
        if (item.itemType == ListItemEnum.VIBE && item.item is ListItemWrapper.VibeChoice) {
            if (item.item.vibe.isEndDateExpired()) {
                itemExpiredSize.addAndGet(1)
            } else if (item.item.vibe.isFinished()){
                itemFinishedSize.addAndGet(1)
            } else {
                itemWaitingSize.addAndGet(1)
            }
        }
//        checkItemEmpty()
//        checkDelimiterWaiting()
//        checkDelimiterFinished()
    }

    override fun removeListItem(holder: HolderVibe) {
        val item = super.itemList[holder.layoutPosition]
        if (item.itemType == ListItemEnum.VIBE && item.item is ListItemWrapper.VibeChoice) {
            if (item.item.vibe.isEndDateExpired()) {
                itemExpiredSize.addAndGet(-1)
            } else if (item.item.vibe.isFinished()){
                itemFinishedSize.addAndGet(-1)
            } else {
                itemWaitingSize.addAndGet(-1)
            }
        }
        super.removeListItem(holder)
//        checkItemEmpty()
//        checkDelimiterWaiting()
//        checkDelimiterFinished()
    }

    private fun checkItemEmpty() {
        if (itemExpiredSize.get() + itemFinishedSize.get() + itemWaitingSize.get() == 0) {
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

    private fun checkDelimiterWaiting() {
        if (itemExpiredSize.get() > 0 && (itemFinishedSize.get() > 0 || itemWaitingSize.get() > 0)) {
            if (!delimiterWaitingView) {
                addDelimiterWaiting()
                delimiterWaitingView = true
            }
        } else {
            if (delimiterWaitingView) {
                removeDelimiterWaiting()
                delimiterWaitingView = false
            }
        }
    }

    private fun checkDelimiterFinished() {
        if (itemWaitingSize.get() > 0 && itemFinishedSize.get() > 0) {
            if (!delimiterFinishedView) {
                addDelimiterFinished()
                delimiterFinishedView = true
            }
        } else {
            if (delimiterFinishedView) {
                removeDelimiterFinished()
                delimiterFinishedView = false
            }
        }
    }

    private fun addItemEmpty() {
        super.updateListItems(itemEmpty)
    }

    private fun removeItemEmpty() {
        super.removeListItem(itemList.indexOf(itemEmpty))
    }

    private fun addDelimiterWaiting() {
        super.updateListItems(delimiterWaiting)
    }

    private fun removeDelimiterWaiting() {
        super.removeListItem(itemList.indexOf(delimiterWaiting))
    }

    private fun addDelimiterFinished() {
        super.updateListItems(delimiterFinished)
    }

    private fun removeDelimiterFinished() {
        super.removeListItem(itemList.indexOf(delimiterFinished))
    }
}