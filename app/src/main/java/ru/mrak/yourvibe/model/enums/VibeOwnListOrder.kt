package ru.mrak.yourvibe.model.enums

import ru.mrak.yourvibe.model.ListItemWrapper
import java.util.Date

enum class VibeOwnListOrder {
    ITEM_EMPTY,
    TIME_END,
    DELIMITER_WAITING,
    WAITING,
    DELIMITER_FINISHED,
    FINISHED,
    ;

    companion object {
        fun getOrder(item: ListItemWrapper): VibeOwnListOrder {
            return if (item.itemType == ListItemEnum.ITEM_EMPTY) {
                ITEM_EMPTY
            } else if (
                item.itemType == ListItemEnum.VIBE &&
                item.item is ListItemWrapper.VibeChoice &&
                item.item.vibe.isEndDateExpired()
            ) {
                TIME_END
            } else if (item.itemType == ListItemEnum.DELIMITER_WAITING) {
                DELIMITER_WAITING
            } else if (
                item.itemType == ListItemEnum.VIBE &&
                item.item is ListItemWrapper.VibeChoice &&
                item.item.vibe.isActual()
            ) {
                WAITING
            } else if (item.itemType == ListItemEnum.DELIMITER_FINISHED) {
                DELIMITER_FINISHED
            } else if (
                item.itemType == ListItemEnum.VIBE &&
                item.item is ListItemWrapper.VibeChoice &&
                item.item.vibe.isFinished()
            ) {
                FINISHED
            } else {
                throw RuntimeException()
            }
        }
    }
}