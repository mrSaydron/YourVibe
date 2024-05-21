package ru.mrak.yourvibe.ui.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.databinding.FragmentStatisticBinding
import ru.mrak.yourvibe.service.StatisticService
import ru.mrak.yourvibe.ui.base.BackFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY

class StatisticFragment : BackFragment() {

    private lateinit var binding: FragmentStatisticBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        APP_ACTIVITY.title = "Статистика"

        viewLifecycleOwner.lifecycleScope.launch { StatisticService.init() }
        initFields()
    }

    private fun initFields() {
        StatisticService.correctAnswerPercent.setListener {
            binding.correctAnswerPercent.text = "Правильных ответов: ${it.first}% (${it.second})"
        }
        StatisticService.wrongAnswerPercent.setListener {
            binding.wrongAnswerPercent.text = "Неправильных ответов: ${it.first}% (${it.second})"
        }

        StatisticService.votedCount.setListener {
            binding.votedCount.text = APP_ACTIVITY.resources.getQuantityString(
                R.plurals.voted_count_string, it, it
            )
        }
        StatisticService.votedInWaiting.setListener {
            binding.votedInWaiting.text = "Проголосованные вайбы ожидают результата: $it"
        }

        StatisticService.createdCount.setListener {
            binding.createdCount.text = APP_ACTIVITY.resources.getQuantityString(
                R.plurals.created_count_string, it, it
            )
        }
        StatisticService.createdInWaiting.setListener {
            binding.createdInWaiting.text = "Созданные вайбы ожидают результат: $it"
        }
        StatisticService.createdInExpired.setListener {
            binding.createdInExpired.text = "Вайбов без результата, с истекшим строком: $it"
        }

        StatisticService.votesGiven.setListener {
            binding.votesGiven.text = "Всего было отдано голосов за Ваши вайбы: $it"
        }
        StatisticService.votesGivenAverage.setListener {
            binding.votesGivenAverage.text =
                String.format("В среднем проголосовали за вайб: %.1f", it)
        }
        StatisticService.resultSamePercent.setListener {
            binding.resultSamePercent.text = "Результат и результат голосования совпадает в: $it% случаев"
        }
    }
}