package ru.mrak.yourvibe.ui.vibe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import ru.mrak.yourvibe.MainActivity
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.databinding.FragmentVibeCreateBinding
import ru.mrak.yourvibe.model.Vibe
import ru.mrak.yourvibe.service.VibeService
import ru.mrak.yourvibe.ui.base.BackFragment
import ru.mrak.yourvibe.ui.listVibe.ListVibeFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.TextChangeWatcher
import ru.mrak.yourvibe.util.replaceFragment
import ru.mrak.yourvibe.util.showToast
import java.time.LocalDate
import java.time.ZoneId

class VibeCreateFragment : BackFragment() {

    private lateinit var binding: FragmentVibeCreateBinding

    private var endDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVibeCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        APP_ACTIVITY.title = "Новое предположение"
        binding.vibeCreateEndDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            endDate = LocalDate.of(year, month + 1, dayOfMonth)
        }
        binding.vibeCreateTitle.addTextChangedListener(
            TextChangeWatcher {
                it?.let {
                    if (it.length > 64) {
                        showToast("Длина заголовка не может быть длинней 64 символов")
                        binding.vibeCreateTitle.setText(it.substring(64))
                    }
                }
            })

        binding.vibeCreateDescription.addTextChangedListener(
            TextChangeWatcher {
                it?.let {
                    if (it.length > 1024) {
                        showToast("Длина описания не может быть длинней 1024 символов")
                        binding.vibeCreateDescription.setText(it.substring(1024))
                    }
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.menu_btn_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_btn_confirm -> create()
        }
        return true
    }

    private fun create() {
        if (validate()) {
            val vibe = Vibe()
            vibe.title = binding.vibeCreateTitle.text.toString()
            vibe.description = binding.vibeCreateDescription.text.toString()
            vibe.endDate = endDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000

            VibeService.saveVibe(vibe) {
                replaceFragment(ListVibeFragment())
            }
        }
    }

    private fun validate(): Boolean {
        return validateTitle() && validateDescription() && validateEndDate()
    }

    private fun validateTitle(): Boolean {
        val title = binding.vibeCreateTitle.text.trim()
        if (title.isEmpty()) {
            showToast("Заголовок пустой")
            return false
        }
        return true
    }

    private fun validateDescription(): Boolean {
        if (binding.vibeCreateDescription.text.trim().isEmpty()) {
            showToast("Описание пустое")
            return false
        }
        return true
    }

    private fun validateEndDate(): Boolean {
        if (endDate < LocalDate.now()) {
            showToast("Дата окончания предположения должна быть больше текущей")
            return false
        }
        return true
    }

}