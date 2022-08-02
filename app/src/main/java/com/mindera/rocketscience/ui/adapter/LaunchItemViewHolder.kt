package com.mindera.rocketscience.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mindera.rocketscience.R
import com.mindera.rocketscience.databinding.LaunchViewItemBinding
import com.mindera.rocketscience.domain.convertUnixToLocalDateTime
import com.mindera.rocketscience.domain.dateFormatter
import com.mindera.rocketscience.domain.daysBetween
import com.mindera.rocketscience.model.Launch
import java.time.LocalDateTime
import kotlin.math.abs

class LaunchItemViewHolder private constructor(val binding: LaunchViewItemBinding) : RecyclerView.ViewHolder(binding.root){
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
        fun from(parent: ViewGroup): LaunchItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = LaunchViewItemBinding.inflate(layoutInflater, parent, false)
            return LaunchItemViewHolder(binding)
        }
    }
}