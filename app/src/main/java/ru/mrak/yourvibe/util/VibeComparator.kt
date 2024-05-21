package ru.mrak.yourvibe.util

import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.enums.VibeOwnListOrder

object VibeComparator {
    val vibeId = compareBy<ListItemWrapper> {
        if (it.item is ListItemWrapper.VibeChoice) {
            it.item.vibe.id
        } else {
            ""
        }
    }

    val vibeOwner = compareBy<ListItemWrapper>(
        {
            VibeOwnListOrder.getOrder(it)
        },
        {
            if (it.item is ListItemWrapper.VibeChoice) {
                it.item.vibe.id
            } else {
                ""
            }
        }
    )

    val vibeHistory = compareBy<ListItemWrapper>(
        { it.itemType.id * -1 },
        { (it.item as ListItemWrapper.VibeChoice).vibe.id }
    )
}