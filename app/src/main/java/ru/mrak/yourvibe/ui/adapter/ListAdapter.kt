package ru.mrak.yourvibe.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mrak.yourvibe.model.ListItemWrapper
import ru.mrak.yourvibe.ui.holder.HolderFactory
import ru.mrak.yourvibe.ui.holder.HolderAbstract
import ru.mrak.yourvibe.ui.holder.HolderVibe
import ru.mrak.yourvibe.util.EventListener
import ru.mrak.yourvibe.util.EventSingleValue
import ru.mrak.yourvibe.util.insert

open class ListAdapter(
    private val longClickListener: ((ListItemWrapper) -> Unit)? = null,
    private val comparator: Comparator<in ListItemWrapper>
) : RecyclerView.Adapter<HolderAbstract>() {

    val itemList = mutableListOf<ListItemWrapper>()
    private val eventListener = EventListener()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderAbstract {
        return HolderFactory.get(parent, viewType, longClickListener, eventListener)
    }

    override fun onBindViewHolder(holder: HolderAbstract, position: Int) {
        holder.initFields(itemList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].itemType.id
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    open fun updateListItems(item: ListItemWrapper) {
        itemList.add(item)
        val position = itemList.insert(item, comparator)
        notifyItemInserted(position)
    }

    open fun removeListItem(holder : HolderVibe) {
        removeListItem(holder.layoutPosition)
    }

    fun removeListItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }
}