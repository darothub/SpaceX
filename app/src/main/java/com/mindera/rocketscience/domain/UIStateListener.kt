package com.mindera.rocketscience.domain

interface UIStateListener {
    fun <T> onSuccess(data: T)
    fun onError(error: String?)
    fun loading()
}