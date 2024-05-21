package ru.mrak.yourvibe.ui.holder

import android.view.View
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.databinding.VibeListItemBinding
import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.Vibe
import ru.mrak.yourvibe.model.enums.ChoiceStatus
import ru.mrak.yourvibe.model.enums.VibeStatus
import ru.mrak.yourvibe.repository.USER
import ru.mrak.yourvibe.util.EventListener
import ru.mrak.yourvibe.util.asDateString
import java.util.Date
import kotlin.math.roundToInt

class HolderVibe(
    view: View,
    private val eventListener: EventListener,
    private val longClickListener: ((ListItemWrapper) -> Unit)? = null,
) : HolderAbstract(view) {
    private val binding: VibeListItemBinding = VibeListItemBinding.bind(view)
    private lateinit var item: ListItemWrapper
    lateinit var vibe: Vibe
    private lateinit var checkedStatus: ChoiceStatus

    private var isSmall = true

    override fun initFields(item: ListItemWrapper) {
        if (item.item is ListItemWrapper.VibeChoice) {
            this.item = item
            this.vibe = item.item.vibe
            this.checkedStatus = item.item.choice
            binding.vibeListItemTitle.text = vibe.title
            binding.vibeListItemDescription.text = vibe.description
            binding.vibeListItemEndDate.text = vibe.endDate.toString().asDateString()
            eventListener.addListener(vibe.id, this::event)
            binding.vibeListItem.setBackgroundResource(setBackgroundId(checkedStatus))
            setVisibility()
            binding.vibeListItemAgreePercent.text = getAgreePercent()
            binding.vibeListItemDisagreePercent.text = getDisagreePercent()

            binding.vibeListItem.setOnClickListener {
                eventListener.event(vibe.id)
                changeSize()
            }
            longClickListener?.let { binding.vibeListItem.setOnLongClickListener {
                it(item)
                true
            } }
        }
    }

    private fun getDisagreePercent(): String {
        if (vibe.agreeCount + vibe.disagreeCount == 0) return "0%"
        val percent =
            (vibe.disagreeCount * 100 / (vibe.agreeCount + vibe.disagreeCount + 0.0)).roundToInt()
        return "${percent}%"
    }

    private fun getAgreePercent(): String {
        if (vibe.agreeCount + vibe.disagreeCount == 0) return "0%"
        val percent =
            (vibe.agreeCount * 100 / (vibe.agreeCount + vibe.disagreeCount + 0.0)).roundToInt()
        return "${percent}%"
    }

    private fun setBackgroundId(checkedStatus: ChoiceStatus): Int {
        return when (vibe.status) {
            VibeStatus.ACTUAL.name -> {
                when (checkedStatus) {
                    ChoiceStatus.NOT_CHECKED -> R.drawable.item_blank
                    ChoiceStatus.IS_AGREE -> R.drawable.item_agree
                    ChoiceStatus.IS_DISAGREE -> R.drawable.item_disagree
                }
            }
            VibeStatus.FINISH.name -> {
                when (checkedStatus) {
                    ChoiceStatus.NOT_CHECKED -> if (true == vibe.result) R.drawable.item_true else R.drawable.item_false
                    ChoiceStatus.IS_AGREE -> if (true == vibe.result) R.drawable.item_true_win else R.drawable.item_false_loser
                    ChoiceStatus.IS_DISAGREE -> if (true == vibe.result) R.drawable.item_true_loser else R.drawable.item_false_win
                }
            }
            else -> R.drawable.item_blank
        }
    }

    private fun setVisibility() {
        binding.vibeListItemEndDate.visibility = View.GONE
        binding.vibeListItemAgreePercent.visibility = View.GONE
        binding.vibeListItemDisagreePercent.visibility = View.GONE
        binding.vibeListItemIconOwner.visibility = View.GONE
        binding.vibeListItemIconOwner.visibility = View.GONE

        // Дата окончания
        if (vibe.status == VibeStatus.ACTUAL.name) binding.vibeListItemEndDate.visibility = View.VISIBLE

        // Результаты
        if (vibe.status == VibeStatus.FINISH.name) {
            binding.vibeListItemAgreePercent.visibility = View.VISIBLE
            binding.vibeListItemDisagreePercent.visibility = View.VISIBLE
        }

        // Иконка владельца
        if (vibe.ownerId == USER.uid) {
            binding.vibeListItemIconOwner.visibility = View.VISIBLE
        }

        // Иконка окончания
        if (vibe.status == VibeStatus.ACTUAL.name && vibe.endDate.toString().toLong() < Date().time) {
            binding.vibeListItemIconAttention.visibility = View.VISIBLE
        }
    }

    private fun event(key: String) {
        if (vibe.id != key) {
            toSmall()
        }
    }

    private fun changeSize() {
        if (isSmall) {
            toBig()
        } else {
            toSmall()
        }
    }

    private fun toSmall() {
        if (!isSmall) {
            isSmall = !isSmall
            binding.vibeListItemDescription.maxLines = 1
        }
    }

    private fun toBig() {
        if (isSmall) {
            isSmall = !isSmall
            binding.vibeListItemDescription.maxLines = 10
        }
    }
}