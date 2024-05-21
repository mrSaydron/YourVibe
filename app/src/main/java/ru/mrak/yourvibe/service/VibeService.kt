package ru.mrak.yourvibe.service

import ru.mrak.yourvibe.model.Vibe
import ru.mrak.yourvibe.model.enums.VibeStatus
import ru.mrak.yourvibe.repository.CURRENT_UID
import ru.mrak.yourvibe.repository.Database
import ru.mrak.yourvibe.repository.NODE_OWNER
import ru.mrak.yourvibe.repository.NODE_VIBES
import ru.mrak.yourvibe.repository.USER
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.showToast

object VibeService {
    fun saveVibe(vibe: Vibe, function: () -> Unit) {
        val key = Database.getKey()
        vibe.id = key
        vibe.ownerId = CURRENT_UID
        vibe.createDate = Database.getCurrentTime()
        vibe.status = VibeStatus.ACTUAL.name

        Database.save(key, vibe) {
            setOwner(key, function)
        }
    }

    fun updateVibe(vibe: Vibe, function: () -> Unit) {
        Database.save(vibe.id, vibe, function)
    }

    fun vibeListener(function: (Vibe) -> Unit) {
        Database.getValueListener(Vibe::class.java) {
            if (it is Vibe) {
                function(it)
            }
        }
    }

    fun ownVibeListener(function: (String) -> Unit) {
        Database.getValueListener("$NODE_OWNER/${USER.uid}", String::class.java) {
            if (it is String) {
                function(it)
            }
        }
    }

    fun getVibe(vibeId: String, function: (Vibe) -> Unit) {
        Database.getSingleListener("$NODE_VIBES/$vibeId", Vibe::class.java) {
            if (it is Vibe) {
                function(it)
            }
        }
    }

    //todo delete
//    fun getChoices(vibeId: String, function: (Boolean?) -> Unit) {
//        Database.getSingleListener("$NODE_CHOICE/${USER.uid}/$vibeId", Boolean::class.java) {
//            if (it is Boolean) {
//                function(it)
//            } else {
//                function(null)
//            }
//        }
//    }

    private fun setOwner(vibeId: String, function: () -> Unit) {
        Database.saveOneValue("$NODE_OWNER/${USER.uid}/${vibeId}", vibeId, function)
    }

    private fun validateResult(vibe: Vibe): Boolean {
        return if (vibe.resultDescription.isEmpty()) {
            showToast("Необходимо заполнить поле результатов")
            false
        } else {
            true
        }
    }

    fun setTrueResult(vibe: Vibe, function: (result: Boolean) -> Unit) {
        if (validateResult(vibe)) {
            vibe.status = VibeStatus.FINISH.name
            vibe.result = true
            vibe.resultDate = Database.getCurrentTime()
            updateVibe(vibe) {
                function(true)
            }
        } else {
            function(false)
        }
    }

    fun setFalseResult(vibe: Vibe, function: (result: Boolean) -> Unit) {
        if (validateResult(vibe)) {
            vibe.status = VibeStatus.FINISH.name
            vibe.result = false
            vibe.resultDate = Database.getCurrentTime()
            updateVibe(vibe) {
                function(true)
            }
        } else {
            function(false)
        }
    }
}