package ru.mrak.yourvibe.ui.vibe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import ru.mrak.yourvibe.MainActivity
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.databinding.FragmentVibeSetResultBinding
import ru.mrak.yourvibe.model.Vibe
import ru.mrak.yourvibe.model.enums.VibeStatus
import ru.mrak.yourvibe.repository.Database
import ru.mrak.yourvibe.service.VibeService
import ru.mrak.yourvibe.ui.base.BackFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.asDateString
import ru.mrak.yourvibe.util.showToast

class VibeSetResultFragment(private val vibe: Vibe) : BackFragment() {

    private lateinit var binding: FragmentVibeSetResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVibeSetResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        APP_ACTIVITY.title = "Результат"
        binding.title.text = vibe.title
        binding.description.text = vibe.description
        binding.dateEnd.text = vibe.endDate.toString().asDateString()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.menu_true_false, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_btn_true -> trueResult()
            R.id.menu_btn_false -> falseResult()
        }
        return true
    }

    private fun falseResult() {
        if (validationDescription()) {
            vibe.status = VibeStatus.FINISH.name
            vibe.result = false
            vibe.resultDescription = binding.resultDescription.text.toString()
            vibe.resultDate = Database.getCurrentTime()
            VibeService.updateVibe(vibe) {
                APP_ACTIVITY.supportFragmentManager.popBackStack()
            }
        }
    }

    private fun trueResult() {
        if (validationDescription()) {
            vibe.status = VibeStatus.FINISH.name
            vibe.result = true
            vibe.resultDescription = binding.resultDescription.text.toString()
            vibe.resultDate = Database.getCurrentTime()
            VibeService.updateVibe(vibe) {
                APP_ACTIVITY.supportFragmentManager.popBackStack()
            }
        }
    }

    private fun validationDescription(): Boolean {
        return if (binding.resultDescription.text.toString().isEmpty()) {
            showToast("Необходимо заполнить поле результатов")
            false
        } else {
            true
        }
    }
}