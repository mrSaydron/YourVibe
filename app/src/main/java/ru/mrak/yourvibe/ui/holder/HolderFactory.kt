package ru.mrak.yourvibe.ui.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.enums.ListItemEnum
import ru.mrak.yourvibe.util.EventListener

object HolderFactory {
    fun get(
        parent: ViewGroup,
        viewType: Int,
        longClickListener: ((ListItemWrapper) -> Unit)? = null,
        eventListener: EventListener,
    ): HolderAbstract {
        return when (viewType) {
            ListItemEnum.VIBE.id -> HolderVibe(
                LayoutInflater.from(parent.context).inflate(R.layout.vibe_list_item, parent, false),
                eventListener,
                longClickListener,
            )
            ListItemEnum.STATISTIC.id -> HolderStatistic(
                LayoutInflater.from(parent.context).inflate(R.layout.statistic_item, parent, false),
                longClickListener,
            )
            ListItemEnum.ITEM_EMPTY.id -> HolderEmpty(
                LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
            )
            ListItemEnum.DELIMITER_WAITING.id -> HolderDelimiter(
                LayoutInflater.from(parent.context).inflate(R.layout.item_delimiter, parent, false)
            )
            ListItemEnum.DELIMITER_FINISHED.id -> HolderDelimiter(
                LayoutInflater.from(parent.context).inflate(R.layout.item_delimiter, parent, false)
            )
            else -> throw RuntimeException()
        }
    }
}