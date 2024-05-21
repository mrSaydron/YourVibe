package ru.mrak.yourvibe.ui.holder

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.mrak.yourvibe.databinding.StatisticItemBinding
import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.service.StatisticService
import ru.mrak.yourvibe.util.APP_ACTIVITY

class HolderStatistic(
    view: View,
    private val longClickListener: ((ListItemWrapper) -> Unit)? = null,
) :  HolderAbstract(view) {
    private val binding: StatisticItemBinding = StatisticItemBinding.bind(view)

    override fun initFields(item: ListItemWrapper) {
        binding.root.visibility = View.GONE
        binding.correctAnswerPercent.visibility = View.GONE
        binding.votedInWaiting.visibility = View.GONE
        binding.createdInWaiting.visibility = View.GONE

        StatisticService.correctAnswerPercent.setListener { correct ->
            StatisticService.wrongAnswerPercent.setListener { wrong ->
                if (correct.second + wrong.second > 0) {
                    binding.correctAnswerPercent.text = "Правильных ответов: ${correct.first}%"
                    binding.root.visibility = View.VISIBLE
                    binding.correctAnswerPercent.visibility = View.VISIBLE
                }
            }
        }

        StatisticService.votedInWaiting.setListener {
            if (it > 0) {
                binding.votedInWaiting.text = "Проголосованные вайбы ожидают результата: $it"
                binding.root.visibility = View.VISIBLE
                binding.votedInWaiting.visibility = View.VISIBLE
            }
        }

        StatisticService.createdInWaiting.setListener {
            if (it > 0) {
                binding.createdInWaiting.text = "Созданные вайбы ожидают результат: $it"
                binding.root.visibility = View.VISIBLE
                binding.createdInWaiting.visibility = View.VISIBLE
            }
        }

        longClickListener?.let { binding.root.setOnLongClickListener {
            it(item)
            true
        } }

        APP_ACTIVITY.lifecycleScope.launch { StatisticService.init() }
    }
}