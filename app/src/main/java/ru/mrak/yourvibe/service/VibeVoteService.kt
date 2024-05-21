package ru.mrak.yourvibe.service

import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.Vibe
import ru.mrak.yourvibe.model.enums.ChoiceStatus
import ru.mrak.yourvibe.model.enums.VibeStatus
import ru.mrak.yourvibe.repository.Database
import ru.mrak.yourvibe.repository.NODE_VOTED
import ru.mrak.yourvibe.repository.NODE_VIBES
import ru.mrak.yourvibe.repository.USER
import ru.mrak.yourvibe.repository.VALUE_AGREE_COUNT
import ru.mrak.yourvibe.repository.VALUE_DISAGREE_COUNT

object VibeVoteService {
    fun getListener(function: (ListItemWrapper.VibeChoice) -> Unit) {
        Database.getValuePairListener("$NODE_VOTED/${USER.uid}", Boolean::class.java) { vote ->
            Database.getSingleListener("$NODE_VIBES/${vote.first}", Vibe::class.java) { vibe ->
                vibe?.let { it ->
                    if (it.status == VibeStatus.ACTUAL.name) {
                        if (vote.second) {
                            function(ListItemWrapper.VibeChoice(vibe, ChoiceStatus.IS_AGREE))
                        } else {
                            function(ListItemWrapper.VibeChoice(vibe, ChoiceStatus.IS_DISAGREE))
                        }
                    }
                }
            }
        }
    }

    fun get(vibeId: String, function: (Boolean?) -> Unit) {
        Database.getSingleListener("$NODE_VOTED/${USER.uid}/$vibeId", Boolean::class.java, function)
    }

    fun setAgree(vibeId: String, function: () -> Unit) {
        get(vibeId) { vote ->
            if (vote == null) {
                Database.saveOneValue("$NODE_VOTED/${USER.uid}/$vibeId", true) {
                    Database.incrementOneValue("$NODE_VIBES/$vibeId/$VALUE_AGREE_COUNT", 1, function)
                }
            } else if (!vote) {
                Database.saveOneValue("$NODE_VOTED/${USER.uid}/$vibeId", true) {
                    Database.incrementOneValue("$NODE_VIBES/$vibeId/$VALUE_AGREE_COUNT", 1) {
                        Database.incrementOneValue("$NODE_VIBES/$vibeId/$VALUE_DISAGREE_COUNT", -1, function)
                    }
                }
            }
        }
    }

    fun setDisagree(vibeId: String, function: () -> Unit) {
        get(vibeId) { vote ->
            if (vote == null) {
                Database.saveOneValue("$NODE_VOTED/${USER.uid}/$vibeId", false) {
                    Database.incrementOneValue("$NODE_VIBES/$vibeId/$VALUE_DISAGREE_COUNT", 1, function)
                }
            } else if (vote) {
                Database.saveOneValue("$NODE_VOTED/${USER.uid}/$vibeId", false) {
                    Database.incrementOneValue("$NODE_VIBES/$vibeId/$VALUE_DISAGREE_COUNT", 1) {
                        Database.incrementOneValue("$NODE_VIBES/$vibeId/$VALUE_AGREE_COUNT", -1, function)
                    }
                }
            }
        }
    }
}