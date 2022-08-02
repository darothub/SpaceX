package com.mindera.rocketscience.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mindera.rocketscience.R
import com.mindera.rocketscience.databinding.LaunchViewItemBinding
import com.mindera.rocketscience.domain.convertUnixToLocalDateTime
import com.mindera.rocketscience.domain.dateFormatter
import com.mindera.rocketscience.domain.daysBetween
import com.mindera.rocketscience.model.Launch
import com.mindera.rocketscience.model.SpaceXItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import kotlin.math.abs

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
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> LaunchesViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LaunchesViewHolder -> {
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

class DataDiffUtilCallback : DiffUtil.ItemCallback<SpaceXItem>() {
    override fun areItemsTheSame(oldItem: SpaceXItem, newItem: SpaceXItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: SpaceXItem, newItem: SpaceXItem): Boolean {
        return oldItem == newItem
    }
}

class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): HeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.launches_header, parent, false)
            return HeaderViewHolder(view)
        }
    }
}

class LaunchesViewHolder private constructor(val binding: LaunchViewItemBinding) : RecyclerView.ViewHolder(binding.root){
    @SuppressLint("StringFormatMatches")
    fun bind(item: Launch, clickListener: LaunchItemListener) {
        val dateFormat = dateFormatter(item.launchDateUnix)
        val launchLocalDateTime = convertUnixToLocalDateTime(item.launchDateUnix)
        val today = LocalDateTime.now()
        val days = daysBetween(today, launchLocalDateTime)
        val sign = if (days > 0) "-" else "+"
        val pastOrFuture = if (days > 0) "From" else "Since"
        val context = binding.root.context
        binding.apply {
            missionName.text = context.getString(R.string.mission, item.missionName)
            missionDateTime.text = context.getString(R.string.date_time, dateFormat.first, dateFormat.second)
            missionRocket.text = context.getString(R.string.rocket, item.rocket?.rocketName, item.rocket?.rocketType)
            missionDays.text = context.getString(R.string.days, "$sign${abs(days)}", pastOrFuture)
            missionImage.load(item.links?.missionPatch)
            if(item.launchSuccess == true)
                missionResult.setImageResource(R.drawable.ic_baseline_check_24)
            else missionResult.setImageResource(R.drawable.ic_baseline_clear_24)
        }
        binding.root.setOnClickListener {
            clickListener.onClick(item, it)
        }
    }

    companion object {
        fun from(parent: ViewGroup): LaunchesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = LaunchViewItemBinding.inflate(layoutInflater, parent, false)
            return LaunchesViewHolder(binding)
        }
    }
}
fun interface LaunchItemListener {
    fun onClick(launch: Launch, view: View)
}

