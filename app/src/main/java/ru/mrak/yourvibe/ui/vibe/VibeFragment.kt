package ru.mrak.yourvibe.ui.vibe

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import ru.mrak.yourvibe.MainActivity
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.databinding.FragmentVibeBinding
import ru.mrak.yourvibe.model.Vibe
import ru.mrak.yourvibe.model.enums.VibeStatus
import ru.mrak.yourvibe.service.UserService
import ru.mrak.yourvibe.service.VibeService
import ru.mrak.yourvibe.service.VibeVoteService
import ru.mrak.yourvibe.ui.base.BackFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.asDateString
import ru.mrak.yourvibe.util.showToast

class VibeFragment(val vibe: Vibe) : BackFragment() {

    private lateinit var binding: FragmentVibeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVibeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        setVisible()
        initFields()
        setVoteResult()
        setHasOptionsMenu(true)
    }

    private fun setVisible() {
        binding.agreePercent.visibility = View.GONE
        binding.disagreePercent.visibility = View.GONE
        binding.dateResult.visibility = View.GONE
        binding.result.visibility = View.GONE
        binding.resultDescriptionEditTitle.visibility = View.GONE
        binding.resultDescriptionEdit.visibility = View.GONE
        binding.canEdit.visibility = View.GONE
        binding.isOwner.visibility = View.GONE
        binding.endDateAttention.visibility = View.GONE

        if (vibe.isFinished()) {
            binding.agreePercent.visibility = View.VISIBLE
            binding.disagreePercent.visibility = View.VISIBLE
            binding.dateResult.visibility = View.VISIBLE
            binding.result.visibility = View.VISIBLE
        }

        if (vibe.isOwner()) {
            binding.isOwner.visibility = View.VISIBLE
        }

        if (vibe.isOwner() && vibe.isActual()) {
            binding.resultDescriptionEditTitle.visibility = View.VISIBLE
            binding.resultDescriptionEdit.visibility = View.VISIBLE
        }

        if (vibe.isActual() && vibe.isEndDateExpired()) {
            binding.endDateAttention.visibility = View.VISIBLE
        }
    }

    private fun initFields() {
        // Наименование
        binding.title.text = vibe.title

        // Результат
        binding.disagreePercent.text = "${vibe.disagreePercent()}% (${vibe.disagreeCount})"
        binding.agreePercent.text = "${vibe.agreePercent()}% (${vibe.agreeCount})"
        if (vibe.status == VibeStatus.FINISH.name) {
            vibe.result?.let {  result ->
                if (result) {
                    binding.agreePercent.componentTextStyle = Typeface.BOLD
                    binding.agreePercent.title += " - Результат"
                } else {
                    binding.disagreePercent.componentTextStyle = Typeface.BOLD
                    binding.disagreePercent.title += " - Результат"
                }
            }
        }
        VibeVoteService.get(vibe.id) {
            it?.let { vote ->
                if (vote) {
                    binding.agreePercent.title += " - Ваш выбор"
                } else {
                    binding.disagreePercent.title += " - Ваш выбор"
                }
            }
        }

        // Автор
        UserService.getUser(vibe.ownerId) {
            it?.let { binding.author.text = it.username }
        }

        // Даты
        binding.dateCreate.text = vibe.createDate.toString().asDateString()
        binding.dateResult.text = vibe.resultDate.toString().asDateString()
        binding.dateEnd.text = vibe.endDate.toString().asDateString()

        // Описание
        binding.description.text = vibe.description

        // Результат описание
        binding.result.text = vibe.resultDescription

    }

    private fun setVoteResult() {
        binding.userVoteResult.visibility = View.GONE
        if (vibe.status == VibeStatus.ACTUAL.name) {
            VibeVoteService.get(vibe.id) { vote ->
                vote?.let {
                    binding.userVoteResult.visibility = View.VISIBLE
                    if (it) {
                        binding.userVoteResult.setBackgroundResource(R.drawable.bg_line_true)
                    } else {
                        binding.userVoteResult.setBackgroundResource(R.drawable.bg_line_false)
                    }
                }
            }
        } else {
            VibeVoteService.get(vibe.id) { vote ->
                vote?.let {
                    vibe.result?.let { result ->
                        binding.userVoteResult.visibility = View.VISIBLE
                        if (vote == result) {
                            binding.userVoteResult.setBackgroundResource(R.drawable.bg_line_true)
                        } else {
                            binding.userVoteResult.setBackgroundResource(R.drawable.bg_line_false)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (vibe.isActual()) {
            (activity as MainActivity).menuInflater.inflate(R.menu.menu_vibe_view, menu)

            menu.findItem(R.id.menu_vibe_view_agree).isVisible = true
            menu.findItem(R.id.menu_vibe_view_disagree).isVisible = true

            menu.findItem(R.id.menu_vibe_view_true).isVisible = false
            menu.findItem(R.id.menu_vibe_view_false).isVisible = false
            menu.findItem(R.id.menu_vibe_view_edit).isVisible = false
            menu.findItem(R.id.menu_vibe_view_delete).isVisible = false

            if (vibe.isOwner()) {
                menu.findItem(R.id.menu_vibe_view_true).isVisible = true
                menu.findItem(R.id.menu_vibe_view_false).isVisible = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_vibe_view_agree -> agree()
            R.id.menu_vibe_view_disagree -> disagree()
            R.id.menu_vibe_view_true -> resultTrue()
            R.id.menu_vibe_view_false -> resultFalse()
        }
        return true
    }

    private fun agree() {
        VibeVoteService.setAgree(vibe.id) {
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
    }

    private fun disagree() {
        VibeVoteService.setDisagree(vibe.id) {
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
    }

    private fun resultTrue() {
        vibe.resultDescription = binding.resultDescriptionEdit.text.toString()
        VibeService.setTrueResult(vibe) { result ->
            if (result) {
                APP_ACTIVITY.supportFragmentManager.popBackStack()
            }
        }
    }

    private fun resultFalse() {
        vibe.resultDescription = binding.resultDescriptionEdit.text.toString()
        VibeService.setFalseResult(vibe) { result ->
            if (result) {
                APP_ACTIVITY.supportFragmentManager.popBackStack()
            }
        }
    }
}