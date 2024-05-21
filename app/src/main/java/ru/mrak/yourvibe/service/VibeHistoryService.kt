package ru.mrak.yourvibe.service

import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.Vibe
import ru.mrak.yourvibe.model.enums.ChoiceStatus
import ru.mrak.yourvibe.model.enums.VibeStatus
import ru.mrak.yourvibe.repository.Database
import ru.mrak.yourvibe.repository.NODE_VOTED
import ru.mrak.yourvibe.repository.NODE_VIBES
import ru.mrak.yourvibe.repository.USER

object VibeHistoryService {
    fun getListener(function: (ListItemWrapper.VibeChoice) -> Unit) {
        Database.getValuePairListener("$NODE_VOTED/${USER.uid}", Boolean::class.java) { choice ->
            Database.getSingleListener("$NODE_VIBES/${choice.first}", Vibe::class.java) { vibe ->
                vibe?.let { it ->
                    if (it.status == VibeStatus.FINISH.name) {
                        if (choice.second) {
                            function(ListItemWrapper.VibeChoice(it, ChoiceStatus.IS_AGREE))
                        } else {
                            function(ListItemWrapper.VibeChoice(it, ChoiceStatus.IS_DISAGREE))
                        }
                    }
                }
            }
        }
    }

    fun set(vibeId: String, isTrue: Boolean, function: () -> Unit) {
        Database.saveOneValue("$NODE_VOTED/${USER.uid}/$vibeId", isTrue, function)
    }

    fun get(vibeId: String, function: (Boolean?) -> Unit) {
        Database.getSingleListener("$NODE_VOTED/${USER.uid}/$vibeId", Boolean::class.java, function)
    }
}