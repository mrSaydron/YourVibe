package ru.mrak.yourvibe.ui.listVoted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import ru.mrak.yourvibe.MainActivity
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.databinding.FragmentListVibeVotedBinding
import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.enums.ListItemEnum
import ru.mrak.yourvibe.service.VibeVoteService
import ru.mrak.yourvibe.ui.adapter.ListAdapter
import ru.mrak.yourvibe.ui.adapter.ListEmptyAdapter
import ru.mrak.yourvibe.ui.base.BurgerFragment
import ru.mrak.yourvibe.ui.vibe.VibeCreateFragment
import ru.mrak.yourvibe.ui.vibe.VibeFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.VibeComparator
import ru.mrak.yourvibe.util.replaceFragment

class ListVibeVotedFragment : BurgerFragment() {

    private lateinit var binding: FragmentListVibeVotedBinding
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListVibeVotedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        APP_ACTIVITY.title = "Проголосованные вайбы"
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ListEmptyAdapter(
            { replaceFragment(VibeFragment((it as ListItemWrapper.VibeChoice).vibe)) },
            VibeComparator.vibeId
        )
        binding.choiceVibeListRecyclerView.adapter = adapter

        VibeVoteService.getListener { vibeChoice ->
            adapter.updateListItems(ListItemWrapper(ListItemEnum.VIBE, vibeChoice))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.menu_btn_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_btn_add -> addVibe()
        }
        return true
    }

    private fun addVibe() {
        replaceFragment(VibeCreateFragment())
    }
}