package com.mindera.rocketscience.ui.adapter

import android.view.View
import com.mindera.rocketscience.model.Launch

fun interface LaunchItemListener {
    fun onClick(launch: Launch, view: View)
}