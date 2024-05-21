package ru.mrak.yourvibe.service

import ru.mrak.yourvibe.model.Vibe
import ru.mrak.yourvibe.model.enums.VibeStatus
import ru.mrak.yourvibe.repository.Database
import ru.mrak.yourvibe.repository.NODE_OWNER
import ru.mrak.yourvibe.repository.NODE_VIBES
import ru.mrak.yourvibe.repository.NODE_VOTED
import ru.mrak.yourvibe.repository.USER
import ru.mrak.yourvibe.util.EventValue
import kotlin.math.roundToInt

object StatisticService {
    val correctAnswerPercent = EventValue(Pair(0, 0))
    val wrongAnswerPercent = EventValue(Pair(0, 0))

    val votedCount = EventValue(0)
    val votedInWaiting = EventValue(0)

    val createdCount = EventValue(0)
    val createdInWaiting = EventValue(0)
    val createdInExpired = EventValue(0)

    val votesGiven = EventValue(0)
    val votesGivenAverage = EventValue(0.0)
    val resultSamePercent = EventValue(0)

    private var correctAnswerCount = 0
    private var wrongAnswerCount = 0
    private var answersCount = 0

    private var alreadyInit = false

    fun init() {
        if (alreadyInit) return
        alreadyInit = true

        Database.getSingleListener("$NODE_VOTED/${USER.uid}") { voteMap ->
            voteMap.forEach { (id, vote) ->
                Database.getSingleListener("$NODE_VIBES/$id", Vibe::class.java) { vibe ->
                    if (vibe != null && vibe.status == VibeStatus.FINISH.name) {
                        if (vibe.result != null) {
                            answersCount++
                            if (vibe.result == vote) {
                                correctAnswerCount++
                            } else {
                                wrongAnswerCount++
                            }

                            val correctPercent = (correctAnswerCount * 100 / answersCount.toDouble()).roundToInt()
                            val wrongPercent = (wrongAnswerCount * 100 / answersCount.toDouble()).roundToInt()
                            correctAnswerPercent.set(Pair(correctPercent, correctAnswerCount))
                            wrongAnswerPercent.set(Pair(wrongPercent, wrongAnswerCount))

                            votedCount.set(votedCount.get() + 1)
                        }
                    }

                    if (vibe != null && vibe.status == VibeStatus.ACTUAL.name) {
                        votedInWaiting.set(votedInWaiting.get() + 1)
                    }
                }
            }
        }

        Database.getSingleListener("$NODE_OWNER/${USER.uid}") { ownerMap ->
            ownerMap.forEach { (id, _) ->
                createdCount.set(createdCount.get() + 1)
                Database.getSingleListener("$NODE_VIBES/$id", Vibe::class.java) { vibe ->
                    vibe?.let {
                        if (vibe.status == VibeStatus.ACTUAL.name) {
                            createdInWaiting.set(createdInWaiting.get() + 1)
                            if (vibe.isEndDateExpired()) {
                                createdInExpired.set(createdInExpired.get() + 1)
                            }
                        }

                        votesGiven.set(votesGiven.get() + vibe.agreeCount + vibe.disagreeCount)
                        votesGivenAverage.set(votesGiven.get() / createdCount.get().toDouble())
                    }
                }
            }
        }
    }
}