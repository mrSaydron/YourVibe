package ru.mrak.yourvibe.model

import ru.mrak.yourvibe.model.enums.VibeStatus
import ru.mrak.yourvibe.repository.CURRENT_UID
import ru.mrak.yourvibe.repository.NODE_VIBES
import ru.mrak.yourvibe.util.firebase.Node
import java.util.Date

@Node(NODE_VIBES)
data class Vibe(
    var id: String = "",
    var ownerId: String = "",
    var title: String = "",
    var description: String = "",
    var createDate: Any = "",
    var endDate: Any = "",
    var status: String = "",
    var resultDate: Any = "",
    var result: Boolean? = null,
    var resultDescription: String = "",
    var agreeCount: Int = 0,
    var disagreeCount: Int = 0,
) {
    fun isEndDateExpired(): Boolean {
        return endDate.toString().toLong() < Date().time
    }

    fun isOwner(): Boolean {
        return ownerId == CURRENT_UID
    }

    fun isFinished(): Boolean {
        return status == VibeStatus.FINISH.name
    }

    fun isActual(): Boolean {
        return status == VibeStatus.ACTUAL.name
    }

    fun agreePercent(): Int {
        val sum = agreeCount + disagreeCount
        if (sum == 0) return 0
        return (agreeCount * 100 / sum)
    }

    fun disagreePercent(): Int {
        val sum = agreeCount + disagreeCount
        if (sum == 0) return 0
        return (disagreeCount * 100 / sum)
    }
}