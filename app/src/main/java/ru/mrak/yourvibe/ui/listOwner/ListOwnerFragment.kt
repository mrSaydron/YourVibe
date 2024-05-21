package ru.mrak.yourvibe.ui.listOwner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import ru.mrak.yourvibe.MainActivity
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.databinding.FragmentListOwnVibeBinding
import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.enums.ChoiceStatus
import ru.mrak.yourvibe.model.enums.ListItemEnum
import ru.mrak.yourvibe.service.VibeService
import ru.mrak.yourvibe.service.VibeVoteService
import ru.mrak.yourvibe.ui.adapter.ListAdapter
import ru.mrak.yourvibe.ui.adapter.ListOwnerAdapter
import ru.mrak.yourvibe.ui.base.BurgerFragment
import ru.mrak.yourvibe.ui.vibe.VibeCreateFragment
import ru.mrak.yourvibe.ui.vibe.VibeFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.VibeComparator
import ru.mrak.yourvibe.util.replaceFragment

class ListOwnerFragment : BurgerFragment() {

    private lateinit var binding: FragmentListOwnVibeBinding
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListOwnVibeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        APP_ACTIVITY.title = "Мои вайбы"

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ListOwnerAdapter {
            replaceFragment(VibeFragment((it.item as ListItemWrapper.VibeChoice).vibe))
        }

        VibeService.ownVibeListener { vibeId ->
            VibeService.getVibe(vibeId) { vibe ->
                VibeVoteService.get(vibeId) { vote ->
                    if (vote == null) {
                        adapter.updateListItems(ListItemWrapper(
                            ListItemEnum.VIBE,
                            ListItemWrapper.VibeChoice(vibe, ChoiceStatus.NOT_CHECKED))
                        )
                    } else if (vote) {
                        adapter.updateListItems(ListItemWrapper(
                            ListItemEnum.VIBE,
                            ListItemWrapper.VibeChoice(vibe, ChoiceStatus.IS_AGREE))
                        )
                    } else {
                        adapter.updateListItems(ListItemWrapper(
                            ListItemEnum.VIBE,
                            ListItemWrapper.VibeChoice(vibe, ChoiceStatus.IS_DISAGREE))
                        )
                    }
                }
            }
        }

        binding.ownVibeListRecyclerView.adapter = adapter
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