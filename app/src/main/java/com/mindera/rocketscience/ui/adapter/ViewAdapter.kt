package com.mindera.rocketscience.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mindera.rocketscience.model.Launch
import com.mindera.rocketscience.model.SpaceXItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class ViewAdapter(private val launchItemListener: LaunchItemListener):
    ListAdapter<SpaceXItem, RecyclerView.ViewHolder>(DataDiffUtilCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Launch>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(SpaceXItem.Header)
                else -> listOf(SpaceXItem.Header) + list.map { SpaceXItem.Launches(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderItemViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> LaunchItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LaunchItemViewHolder -> {
                val launches = getItem(position) as SpaceXItem.Launches
                holder.bind(launches.launch, launchItemListener)
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SpaceXItem.Header -> ITEM_VIEW_TYPE_HEADER
            else -> ITEM_VIEW_TYPE_ITEM
        }
    }
}


