package ru.mrak.yourvibe.ui.listVibe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.mrak.yourvibe.MainActivity
import ru.mrak.yourvibe.R
import ru.mrak.yourvibe.databinding.FragmentListVibeBinding
import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.model.enums.ChoiceStatus
import ru.mrak.yourvibe.model.enums.ListItemEnum
import ru.mrak.yourvibe.model.enums.VibeStatus
import ru.mrak.yourvibe.service.VibeService
import ru.mrak.yourvibe.service.VibeVoteService
import ru.mrak.yourvibe.ui.base.BurgerFragment
import ru.mrak.yourvibe.ui.vibe.VibeCreateFragment
import ru.mrak.yourvibe.ui.adapter.ListAdapter
import ru.mrak.yourvibe.ui.adapter.ListEmptyAdapter
import ru.mrak.yourvibe.ui.holder.HolderVibe
import ru.mrak.yourvibe.ui.vibe.VibeFragment
import ru.mrak.yourvibe.util.APP_ACTIVITY
import ru.mrak.yourvibe.util.VibeComparator
import ru.mrak.yourvibe.util.replaceFragment

class ListVibeFragment : BurgerFragment() {

    private lateinit var binding: FragmentListVibeBinding
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListVibeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        APP_ACTIVITY.title = "Вайбы"

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ListEmptyAdapter(
            { replaceFragment(VibeFragment((it.item as ListItemWrapper.VibeChoice).vibe)) },
            VibeComparator.vibeId
        )
        binding.vibeListRecyclerView.adapter = adapter
        initItemTouch(binding.vibeListRecyclerView)

        VibeService.vibeListener { vibe ->
            if (vibe.status == VibeStatus.ACTUAL.name) {
                VibeVoteService.get(vibe.id) { vote ->
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
    }

    private fun initItemTouch(vibeListRecyclerView: RecyclerView) {
        val touchHelper = ItemTouchHelper(
            object : ItemTouchHelper.Callback() {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    return makeMovementFlags(
                        0,
                        ItemTouchHelper.END or ItemTouchHelper.START
                    )
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (viewHolder is HolderVibe) {
                        when (direction) {
                            ItemTouchHelper.END -> vibeChoiceTrue(viewHolder)
                            ItemTouchHelper.START -> vibeChoiceFalse(viewHolder)
                        }

                        val adapter = vibeListRecyclerView.adapter
                        if (adapter is ListAdapter) {
                            adapter.removeListItem(viewHolder)
                        }
                    }
                }
            }
        )
        touchHelper.attachToRecyclerView(vibeListRecyclerView)
    }

    private fun vibeChoiceTrue(viewHolder: HolderVibe) {
        VibeVoteService.setAgree(viewHolder.vibe.id) {}
    }

    private fun vibeChoiceFalse(viewHolder: HolderVibe) {
        VibeVoteService.setDisagree(viewHolder.vibe.id) {}
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