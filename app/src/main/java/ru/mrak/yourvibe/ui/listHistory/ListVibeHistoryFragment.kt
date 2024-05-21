package ru.mrak.yourvibe.ui.listHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import ru.mrak.yourvibe.MainActivity
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.databinding.FragmentListVibeHistoryBinding
import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.enums.ListItemEnum
import ru.mrak.yourvibe.service.VibeHistoryService
import ru.mrak.yourvibe.ui.adapter.ListAdapter
import ru.mrak.yourvibe.ui.adapter.ListEmptyAdapter
import ru.mrak.yourvibe.ui.base.BurgerFragment
import ru.mrak.yourvibe.ui.statistic.StatisticFragment
import ru.mrak.yourvibe.ui.vibe.VibeCreateFragment
import ru.mrak.yourvibe.ui.vibe.VibeFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.VibeComparator
import ru.mrak.yourvibe.util.replaceFragment

class ListVibeHistoryFragment : BurgerFragment() {

    private lateinit var binding: FragmentListVibeHistoryBinding
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListVibeHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        APP_ACTIVITY.title = "История"

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ListEmptyAdapter (
            {
                if (it.itemType == ListItemEnum.VIBE) {
                    replaceFragment(VibeFragment((it.item as ListItemWrapper.VibeChoice).vibe))
                } else if (it.itemType == ListItemEnum.STATISTIC) {
                    replaceFragment(StatisticFragment())
                }
            },
            VibeComparator.vibeHistory
        )
        binding.vibeOwnerFinishListRecyclerView.adapter = adapter
        adapter.updateListItems(ListItemWrapper(ListItemEnum.STATISTIC, Any()))

        VibeHistoryService.getListener {
            adapter.updateListItems(ListItemWrapper(ListItemEnum.VIBE, it))
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