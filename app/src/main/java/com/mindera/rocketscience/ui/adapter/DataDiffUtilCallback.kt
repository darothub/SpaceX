package com.mindera.rocketscience.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.mindera.rocketscience.model.SpaceXItem

class DataDiffUtilCallback : DiffUtil.ItemCallback<SpaceXItem>() {
    override fun areItemsTheSame(oldItem: SpaceXItem, newItem: SpaceXItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: SpaceXItem, newItem: SpaceXItem): Boolean {
        return oldItem == newItem
    }
}