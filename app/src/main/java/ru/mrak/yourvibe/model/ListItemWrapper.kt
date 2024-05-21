package ru.mrak.yourvibe.model

import ru.mrak.yourvibe.model.enums.ChoiceStatus
import ru.mrak.yourvibe.model.enums.ListItemEnum

data class ListItemWrapper(
    val itemType: ListItemEnum,
    val item: Any,
) {
    data class VibeChoice(
        val vibe: Vibe,
        val choice: ChoiceStatus
    )
}